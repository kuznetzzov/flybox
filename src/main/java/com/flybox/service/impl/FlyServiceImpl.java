package com.flybox.service.impl;

import com.flybox.model.db.entity.Fly;
import com.flybox.model.db.repository.FlyRepo;
import com.flybox.model.dto.request.FlyInfoRequest;
import com.flybox.service.FlyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FlyServiceImpl implements FlyService {

    private final FlyRepo flyRepo;


    @Override
    public FlyInfoRequest getFlyInfo(Long id) {
        Fly fly = flyRepo.findById(id).orElse(null);
        if (fly == null) {
            // Обработка, если мушка не найдена
            return null;
        }
        return convertToFlyInfoRequest(fly);
    }

    @Override
    public List<FlyInfoRequest> getAllFlyInfo() {
        List<Fly> flies = flyRepo.findAll();
        return flies.stream().map(this::convertToFlyInfoRequest).collect(Collectors.toList());
    }

    @Override
    public FlyInfoRequest createFlyInfo(FlyInfoRequest flyInfoRequest) {
        Fly fly = convertToFly(flyInfoRequest);
        fly = flyRepo.save(fly);
        return convertToFlyInfoRequest(fly);
    }

    @Override
    public FlyInfoRequest updateFlyInfo(Long id, FlyInfoRequest flyInfoRequest) {
        Fly fly = flyRepo.findById(id).orElse(null);
        if (fly == null) {
            // Обработка, если мушка не найдена
            return null;
        }
        // Обновление свойств мушки из flyInfoRequest
        fly.setName(flyInfoRequest.getName());
        // Продолжите обновление остальных свойств

        fly = flyRepo.save(fly);
        return convertToFlyInfoRequest(fly);
    }

    @Override
    public void deleteFlyInfo(Long id) {
        flyRepo.deleteById(id);
    }

    private FlyInfoRequest convertToFlyInfoRequest(Fly fly) {
        // Создайте объект FlyInfoRequest и заполните его данными из Fly
        FlyInfoRequest flyInfoRequest = new FlyInfoRequest();
        flyInfoRequest.setId(fly.getId());
        flyInfoRequest.setName(fly.getName());
        // Заполните остальные свойства
        return flyInfoRequest;
    }

    private Fly convertToFly(FlyInfoRequest flyInfoRequest) {
        // Создайте объект Fly и заполните его данными из FlyInfoRequest
        Fly fly = new Fly();
        fly.setId(flyInfoRequest.getId());
        fly.setName(flyInfoRequest.getName());
        // Заполните остальные свойства
        return fly;
    }
}
