package com.delivery.liveasy.controller;

import com.delivery.liveasy.dto.LoadDTO;
import com.delivery.liveasy.model.enums.LoadStatus;
import com.delivery.liveasy.service.LoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Load operations.
 */
@RestController
@RequestMapping("/load")
@Slf4j
public class LoadController {

    @Autowired
    private LoadService loadService;

    /**
     * Create a new load.
     *
     * @param loadDTO the load DTO
     * @return the created load DTO
     */
    @PostMapping
    public ResponseEntity<LoadDTO> createLoad(@Valid @RequestBody LoadDTO loadDTO) {
        log.info("REST request to create a new load");
        LoadDTO createdLoad = loadService.createLoad(loadDTO);
        return new ResponseEntity<>(createdLoad, HttpStatus.CREATED);
    }

    /**
     * Get all loads with optional filtering.
     *
     * @param shipperId the shipper ID (optional)
     * @param truckType the truck type (optional)
     * @param status the load status (optional)
     * @return list of load DTOs
     */
    @GetMapping
    public ResponseEntity<List<LoadDTO>> getLoads(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false) String truckType,
            @RequestParam(required = false) LoadStatus status) {

        log.info("REST request to get loads with filters: shipperId={}, truckType={}, status={}",
                shipperId, truckType, status);

        List<LoadDTO> loads;

        if (shipperId != null) {
            loads = loadService.getLoadsByShipperId(shipperId);
        } else if (truckType != null) {
            loads = loadService.getLoadsByTruckType(truckType);
        } else if (status != null) {
            loads = loadService.getLoadsByStatus(status);
        } else {
            loads = loadService.getAllLoads();
        }

        return ResponseEntity.ok(loads);
    }

    /**
     * Get load by ID.
     *
     * @param loadId the load ID
     * @return the load DTO
     */
    @GetMapping("/{loadId}")
    public ResponseEntity<LoadDTO> getLoadById(@PathVariable UUID loadId) {
        log.info("REST request to get load with ID: {}", loadId);
        LoadDTO load = loadService.getLoadById(loadId);
        return ResponseEntity.ok(load);
    }

    /**
     * Update load.
     *
     * @param loadId the load ID
     * @param loadDTO the load DTO
     * @return the updated load DTO
     */
    @PutMapping("/{loadId}")
    public ResponseEntity<LoadDTO> updateLoad(
            @PathVariable UUID loadId,
            @Valid @RequestBody LoadDTO loadDTO) {

        log.info("REST request to update load with ID: {}", loadId);
        LoadDTO updatedLoad = loadService.updateLoad(loadId, loadDTO);
        return ResponseEntity.ok(updatedLoad);
    }

    /**
     * Delete load.
     *
     * @param loadId the load ID
     * @return no content
     */
    @DeleteMapping("/{loadId}")
    public ResponseEntity<Void> deleteLoad(@PathVariable UUID loadId) {
        log.info("REST request to delete load with ID: {}", loadId);
        loadService.deleteLoad(loadId);
        return ResponseEntity.noContent().build();
    }
}
