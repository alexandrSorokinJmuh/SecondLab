package com.example.offer_sub_system.controllers;

import com.example.offer_sub_system.dto.PaidTypeDto;
import com.example.offer_sub_system.entities.*;
import com.example.offer_sub_system.services.CategoryService;
import com.example.offer_sub_system.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController {
    OfferService offerService;
    CategoryService categoryService;

    @Value("${http.address}")
    String originAddress;
    @Value("${jwt.header}")
    String jwtHeader;




    public OfferController(OfferService offerService, CategoryService categoryService) {
        this.offerService = offerService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("allOffers")
    public List<Offer> getAll() {
        List<Offer> offerList = offerService.getAll();
        return offerList;
    }

    @ModelAttribute("allCategories")
    public List<Category> getCategories() {
        List<Category> categoryList = categoryService.getAll();
        return categoryList;
    }

    @ModelAttribute("allPaidTypes")
    public Map<Integer, PaidTypeDto> getPaidTypes(HttpServletRequest request) {
        // request url
//        String url = originAddress + "/paid_type";
//
//        // create an instance of RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//
//        // create headers
//        HttpHeaders headers = new HttpHeaders();
//        // set `content-type` header
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        // set `accept` header
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        String token = request.getHeader(originAddress);
//        if (token == null){
//            token = Arrays.stream(request.getCookies())
//                    .filter(cookie -> cookie.getName().equals(jwtHeader))
//                    .map(cookie -> cookie.getValue())
//                    .findFirst()
//                    .orElse("");
//        }
//
//        headers.set(jwtHeader, token);
//
//        // build the request
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
//
//        // send POST request
//        ResponseEntity<Object[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object[].class);
//
//        // check response
//        if (response.getStatusCode() == HttpStatus.OK) {
//            System.out.println("Request Successful");
//
//        List<Object> responseBody = Arrays.asList(response.getBody());
//        System.out.println(responseBody.getClass());
//        System.out.println(responseBody.size());
//        return responseBody;
//        for (Object obj : responseBody) {
//            System.out.println(obj);
//            System.out.println(obj.getClass());
//            LinkedHashMap k = (LinkedHashMap) obj;

//        }

//        } else {
//            System.out.println("Request Failed");
//            System.out.println(response.getStatusCode());
//            return null;
//        }

        Map<Integer, PaidTypeDto> responseBody = new HashMap<>();
        responseBody.put(1, new PaidTypeDto("1", "card"));

        responseBody.put(2, new PaidTypeDto("2", "cash"));

        responseBody.put(3,new PaidTypeDto("3", "e-money"));


        return responseBody;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("title", "Offers");
        return "offer/index";
    }

    @GetMapping("/{offer_id}")
    public String show(@PathVariable("offer_id") int id,
                       Model model) {
        Offer offer = offerService.getById(id);
        model.addAttribute("offer", offer);
        Map<Integer, PaidTypeDto> paidTypeDtoMap = (Map<Integer, PaidTypeDto>) model.asMap().get("allPaidTypes");

        List<Integer> offerPaidTypeId =offer.getPaidTypes().stream()
                .map(OfferPaidType::getPaidTypeId)
                .collect(Collectors.toList());
        System.out.println(offerPaidTypeId);
        model.addAttribute("paidTypes", paidTypeDtoMap.entrySet().stream()
                .filter(entry->offerPaidTypeId.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())
        );
        model.addAttribute("title", "Show offer");
        return "offer/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("offer") Offer offer, Model model) {
        model.addAttribute("title", "Create offer");
        return "offer/new";
    }

    @GetMapping("/{offer_id}/edit")
    public String edit(Model model, @PathVariable("offer_id") int id) {
        Offer offer = offerService.getById(id);
        model.addAttribute("offer", offer);
        model.addAttribute("offerCharacteristics", offer.getOfferCharacteristics().stream()
                .map(OfferCharacteristics::getCharacteristic)
                .collect(Collectors.toList())
        );

        model.addAttribute("offerCharacteristicValues", offer.getOfferCharacteristics().stream()
                .map(OfferCharacteristics::getCharacteristicValue)
                .collect(Collectors.toList())
        );

        System.out.println(offer.getPaidTypes());
        model.addAttribute("offerPaidTypes", offer.getPaidTypes().stream()
                .map(OfferPaidType::getPaidTypeId)
                .collect(Collectors.toList())
        );


        model.addAttribute("title", "Edit offer");
        return "offer/edit";
    }


}
