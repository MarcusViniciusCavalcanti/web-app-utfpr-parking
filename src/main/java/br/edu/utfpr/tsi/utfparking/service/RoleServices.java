package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.RoleRepository;
import br.edu.utfpr.tsi.utfparking.models.dtos.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServices {

    private final RoleRepository roleRepository;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> RoleDTO.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
