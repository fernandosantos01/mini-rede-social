package com.example.mini_rede_social.service;

import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.repository.PerfilRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

}
