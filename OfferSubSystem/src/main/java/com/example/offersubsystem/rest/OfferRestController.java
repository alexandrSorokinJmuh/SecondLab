package com.example.offersubsystem.rest;

import com.example.offersubsystem.dto.CharacteristicDto;
import com.example.offersubsystem.dto.LabelAndValueDto;
import com.example.offersubsystem.dto.OfferDto;
import com.example.offersubsystem.entities.Characteristic;
import com.example.offersubsystem.entities.Offer;
import com.example.offersubsystem.services.CharacteristicService;
import com.example.offersubsystem.services.OfferService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferRestController {
    OfferService offerService;
    CharacteristicService characteristicService;

    public OfferRestController(OfferService offerService, CharacteristicService characteristicService) {
        this.offerService = offerService;
        this.characteristicService = characteristicService;
    }

    @GetMapping("/offers")
    public List<Offer> indexRest() {
        return offerService.getAll();
    }

    @GetMapping("/offer/{offer_id}")
    public Offer showRest(@PathVariable("offer_id") int id) {
        return offerService.getById(id);
    }


    @PostMapping("")
    public Offer createRest(Offer offer) {
        offerService.create(offer);
        return offer;
    }

    @PutMapping("/{offer_id}")
    public Offer updateRest(@PathVariable("offer_id") int id,
                            OfferDto offerDto
    ) {
        System.out.println(offerDto);
        Offer offer = offerService.updateWithDto(offerDto);

        return offer;
    }
    @DeleteMapping("/{offer_id}")
    public Offer deleteRest(@PathVariable("offer_id") int id) {
        Offer offer = offerService.getById(id);
        offerService.delete(id);
        return offer;
    }



    @GetMapping("/{offer_id}/characteristicsNotInOffer")

    public List<LabelAndValueDto> showCharacteristicsNotInOfferByTerm(
            @PathVariable("offer_id") int id,
            @RequestParam(value = "term", required = false, defaultValue = "") String term) {
        Offer offer = offerService.getById(id);

        List<LabelAndValueDto> suggestions = characteristicService.getSuggestionsByTerm(characteristicService.getCharacteristicNotInOffer(offer), term);
        return suggestions;
    }
}
