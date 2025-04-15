package com.delivery.liveasy.service;

import com.delivery.liveasy.dto.LoadDTO;
import com.delivery.liveasy.model.enums.LoadStatus;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Load operations.
 */
public interface LoadService {
    
    /**
     * Create a new load.
     *
     * @param loadDTO the load DTO
     * @return the created load DTO
     */
    LoadDTO createLoad(LoadDTO loadDTO);
    
    /**
     * Get all loads.
     *
     * @return list of load DTOs
     */
    List<LoadDTO> getAllLoads();
    
    /**
     * Get loads by shipper ID.
     *
     * @param shipperId the shipper ID
     * @return list of load DTOs
     */
    List<LoadDTO> getLoadsByShipperId(String shipperId);
    
    /**
     * Get loads by truck type.
     *
     * @param truckType the truck type
     * @return list of load DTOs
     */
    List<LoadDTO> getLoadsByTruckType(String truckType);
    
    /**
     * Get loads by status.
     *
     * @param status the load status
     * @return list of load DTOs
     */
    List<LoadDTO> getLoadsByStatus(LoadStatus status);
    
    /**
     * Get load by ID.
     *
     * @param id the load ID
     * @return the load DTO
     */
    LoadDTO getLoadById(UUID id);
    
    /**
     * Update load.
     *
     * @param id the load ID
     * @param loadDTO the load DTO
     * @return the updated load DTO
     */
    LoadDTO updateLoad(UUID id, LoadDTO loadDTO);
    
    /**
     * Delete load.
     *
     * @param id the load ID
     */
    void deleteLoad(UUID id);
    
    /**
     * Update load status.
     *
     * @param id the load ID
     * @param status the load status
     * @return the updated load DTO
     */
    LoadDTO updateLoadStatus(UUID id, LoadStatus status);
}
