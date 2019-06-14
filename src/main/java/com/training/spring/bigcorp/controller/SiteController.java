package com.training.spring.bigcorp.controller;

import com.training.spring.bigcorp.model.Site;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.repository.MeasureDao;
import com.training.spring.bigcorp.repository.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/sites")
@Transactional
public class SiteController {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private MeasureDao measureDao;

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable String id) {
        return new ModelAndView("site").addObject("site", siteDao.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("site").addObject("site", new Site());
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(Site site) {
        if(site.getId() == null) {
            return new ModelAndView("site").addObject("site", siteDao.save(site));
        } else {
            Site siteToPersist = siteDao.findById(site.getId()).orElseThrow(IllegalArgumentException::new);
            //L'utilisateur ne peut changer que le nom du site dur l'écran
            siteToPersist.setName(site.getName());
            return new ModelAndView("sites").addObject("sites", siteDao.findAll());
        }
    }

    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable String id) {
        //Suppression des capteurs liés au site et des measures liées aux capteurs avant de supprimer le site lui-même
        Site site = siteDao.findById(id).orElseThrow(IllegalArgumentException::new);
        site.getCaptors().forEach(c -> measureDao.deleteByCaptorId(c.getId()));
        captorDao.deleteBySiteId(id);
        siteDao.delete(site);
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }


    @GetMapping("/{id}/measures")
    public ModelAndView findMeasuresById(@PathVariable String id) {
        Site site = siteDao.findById(id).orElseThrow(IllegalArgumentException::new);
        // Comme les templates ont une intelligence limitée on concatène ici les id de captor dans une chaine
        // de caractères qui pourra être exeploitée tel quelle
        String captors = site.getCaptors().stream()
                .map(c -> "{ id: '" + c.getId() + "', name: '" + c.getName()
                        + "'}")
                .collect(Collectors.joining(","));
        return new ModelAndView("site-measures")
                .addObject("site", site)
                .addObject("captors", captors);
    }



}
