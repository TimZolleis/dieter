package dev.elektronisch.dieter.server.organisation;

import dev.elektronisch.dieter.common.dto.organisation.Organisation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/organisation")
public final class OrganisationController {

    private final OrganisationService service;
    private final ModelMapper modelMapper;

    public OrganisationController(final OrganisationService service, final ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{organisationId}")
    public ResponseEntity<Organisation> getOrganisation(@PathVariable("organisationId") final UUID organisationId) {
        final OrganisationEntity entity = service.getOrganisation(organisationId);
        final Organisation organisation = convertToDTO(entity);
        return new ResponseEntity<>(organisation, HttpStatus.OK);
    }

    private Organisation convertToDTO(final OrganisationEntity entity) {
        return modelMapper.map(entity, Organisation.class);
    }
}
