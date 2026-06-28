package com.lmalecic.milvshop.controller.rest;

import com.lmalecic.milvshop.dto.NationDto;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.NationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nations")
@RequiredArgsConstructor
public class NationsRestController {

    private final NationService nationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NationDto> getNations() {
        return this.nationService.findAllActive();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public NationDto getNation(@PathVariable Long id) {
        return this.nationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nation with id " + id + " not found!"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public NationDto createNation(@RequestBody NationDto nationDto) {
        return this.nationService.create(nationDto);
    }
}
