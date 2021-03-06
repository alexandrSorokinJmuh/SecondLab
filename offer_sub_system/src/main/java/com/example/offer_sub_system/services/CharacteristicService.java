package com.example.offer_sub_system.services;

import com.example.offer_sub_system.dao.CharacteristicsDao;
import com.example.offer_sub_system.dto.CharacteristicDto;
import com.example.offer_sub_system.dto.LabelAndValueDto;
import com.example.offer_sub_system.entities.Characteristic;
import com.example.offer_sub_system.entities.Offer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacteristicService {
    CharacteristicsDao characteristicsDao;

    public CharacteristicService(CharacteristicsDao characteristicsDao) {
        this.characteristicsDao = characteristicsDao;
    }

    public List<Characteristic> getAll() {
        return characteristicsDao.getAll();
    }

    public Characteristic getById(int id) {
        return characteristicsDao.getById(id);
    }

    public Characteristic create(Characteristic characteristic){

        return characteristicsDao.create(characteristic);
    }

    public Characteristic update(int id, Characteristic characteristic){
        return characteristicsDao.update(id, characteristic);
    }

    public void delete(int id){
        characteristicsDao.delete(id);
    }

    public Characteristic updateWithDto(CharacteristicDto characteristicDto) {
        Characteristic characteristic = new Characteristic();
        characteristic.setName(characteristicDto.getName());
        characteristic.setDescription(characteristicDto.getDescription());

        return update(characteristicDto.getCharacteristic_id(), characteristic);
    }

    public List<Characteristic> getCharacteristicNotInOffer(Offer offer){
        List<Characteristic> res = this.getAll();
        res.removeAll(offer.getOfferCharacteristics().stream()
                .filter(offerCharacteristics -> offerCharacteristics.getOffer().equals(offer))
                .map(offerCharacteristics -> offerCharacteristics.getCharacteristic())
                .collect(Collectors.toList())
        );
        return res;
    }

    public List<LabelAndValueDto> getSuggestionsByTerm(List<Characteristic> characteristicNotInOffer, String term) {
        return characteristicNotInOffer.stream()
                .filter(characteristic -> characteristic.getName().contains(term))
                .map(characteristic -> new LabelAndValueDto(characteristic.getName(), characteristic.getCharacteristic_id().toString()))
                .collect(Collectors.toList());
    }

    public List<Characteristic> getCharacteristicNotLike(String[] characteristics, String term) {
        return characteristicsDao.getCharacteristicNotLike(Arrays.stream(characteristics)
                .map(v -> Integer.parseInt(v)).collect(Collectors.toList()), term);
    }

    public List<LabelAndValueDto> getLabelAndValue(List<Characteristic> characteristics) {
        return characteristics.stream()
                .map(characteristic -> new LabelAndValueDto(characteristic.getName(), characteristic.getCharacteristic_id().toString()))
                .collect(Collectors.toList());
    }
}
