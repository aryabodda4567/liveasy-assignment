package com.delivery.liveasy.service.impl;

import com.delivery.liveasy.dto.FacilityDTO;
import com.delivery.liveasy.dto.LoadDTO;
import com.delivery.liveasy.exception.ResourceNotFoundException;
import com.delivery.liveasy.model.Facility;
import com.delivery.liveasy.model.Load;
import com.delivery.liveasy.model.enums.LoadStatus;
import com.delivery.liveasy.repository.LoadRepository;
import com.delivery.liveasy.service.LoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of LoadService.
 */
@Service
@Slf4j
public class LoadServiceImpl implements LoadService {

    @Autowired
    private LoadRepository loadRepository;

    @Override
    @Transactional
    public LoadDTO createLoad(LoadDTO loadDTO) {
        log.info("Creating load for shipper: {}", loadDTO.getShipperId());

        Load load = mapToEntity(loadDTO);
        load.setStatus(LoadStatus.POSTED); // Default status

        Load savedLoad = loadRepository.save(load);
        log.info("Load created with ID: {}", savedLoad.getId());

        return mapToDTO(savedLoad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoadDTO> getAllLoads() {
        log.info("Fetching all loads");

        return loadRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoadDTO> getLoadsByShipperId(String shipperId) {
        log.info("Fetching loads for shipper: {}", shipperId);

        return loadRepository.findByShipperId(shipperId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoadDTO> getLoadsByTruckType(String truckType) {
        log.info("Fetching loads for truck type: {}", truckType);

        return loadRepository.findByTruckType(truckType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoadDTO> getLoadsByStatus(LoadStatus status) {
        log.info("Fetching loads with status: {}", status);

        return loadRepository.findByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LoadDTO getLoadById(UUID id) {
        log.info("Fetching load with ID: {}", id);

        Load load = loadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", id));

        return mapToDTO(load);
    }

    @Override
    @Transactional
    public LoadDTO updateLoad(UUID id, LoadDTO loadDTO) {
        log.info("Updating load with ID: {}", id);

        Load load = loadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", id));

        // Update fields
        load.setShipperId(loadDTO.getShipperId());
        load.setProductType(loadDTO.getProductType());
        load.setTruckType(loadDTO.getTruckType());
        load.setNoOfTrucks(loadDTO.getNoOfTrucks());
        load.setWeight(loadDTO.getWeight());
        load.setComment(loadDTO.getComment());

        // Update facility
        Facility facility = new Facility();
        facility.setLoadingPoint(loadDTO.getFacility().getLoadingPoint());
        facility.setUnloadingPoint(loadDTO.getFacility().getUnloadingPoint());
        facility.setLoadingDate(loadDTO.getFacility().getLoadingDate());
        facility.setUnloadingDate(loadDTO.getFacility().getUnloadingDate());
        load.setFacility(facility);

        // Don't update status here, use updateLoadStatus method

        Load updatedLoad = loadRepository.save(load);
        log.info("Load updated with ID: {}", updatedLoad.getId());

        return mapToDTO(updatedLoad);
    }

    @Override
    @Transactional
    public void deleteLoad(UUID id) {
        log.info("Deleting load with ID: {}", id);

        Load load = loadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", id));

        loadRepository.delete(load);
        log.info("Load deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public LoadDTO updateLoadStatus(UUID id, LoadStatus status) {
        log.info("Updating load status to {} for load with ID: {}", status, id);

        Load load = loadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", id));

        load.setStatus(status);

        Load updatedLoad = loadRepository.save(load);
        log.info("Load status updated to {} for load with ID: {}", status, id);

        return mapToDTO(updatedLoad);
    }

    /**
     * Map Load entity to LoadDTO.
     *
     * @param load the load entity
     * @return the load DTO
     */
    private LoadDTO mapToDTO(Load load) {
        LoadDTO loadDTO = new LoadDTO();
        loadDTO.setId(load.getId());
        loadDTO.setShipperId(load.getShipperId());

        FacilityDTO facilityDTO = new FacilityDTO();
        facilityDTO.setLoadingPoint(load.getFacility().getLoadingPoint());
        facilityDTO.setUnloadingPoint(load.getFacility().getUnloadingPoint());
        facilityDTO.setLoadingDate(load.getFacility().getLoadingDate());
        facilityDTO.setUnloadingDate(load.getFacility().getUnloadingDate());
        loadDTO.setFacility(facilityDTO);

        loadDTO.setProductType(load.getProductType());
        loadDTO.setTruckType(load.getTruckType());
        loadDTO.setNoOfTrucks(load.getNoOfTrucks());
        loadDTO.setWeight(load.getWeight());
        loadDTO.setComment(load.getComment());
        loadDTO.setDatePosted(load.getDatePosted());
        loadDTO.setStatus(load.getStatus());

        return loadDTO;
    }

    /**
     * Map LoadDTO to Load entity.
     *
     * @param loadDTO the load DTO
     * @return the load entity
     */
    private Load mapToEntity(LoadDTO loadDTO) {
        Load load = new Load();
        load.setShipperId(loadDTO.getShipperId());

        Facility facility = new Facility();
        facility.setLoadingPoint(loadDTO.getFacility().getLoadingPoint());
        facility.setUnloadingPoint(loadDTO.getFacility().getUnloadingPoint());
        facility.setLoadingDate(loadDTO.getFacility().getLoadingDate());
        facility.setUnloadingDate(loadDTO.getFacility().getUnloadingDate());
        load.setFacility(facility);

        load.setProductType(loadDTO.getProductType());
        load.setTruckType(loadDTO.getTruckType());
        load.setNoOfTrucks(loadDTO.getNoOfTrucks());
        load.setWeight(loadDTO.getWeight());
        load.setComment(loadDTO.getComment());

        return load;
    }
}
