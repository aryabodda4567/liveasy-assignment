package com.delivery.liveasy.repository;

import com.delivery.liveasy.model.Load;
import com.delivery.liveasy.model.enums.LoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Load entity.
 */
@Repository
public interface LoadRepository extends JpaRepository<Load, UUID> {
    
    /**
     * Find loads by shipper ID.
     *
     * @param shipperId the shipper ID
     * @return list of loads
     */
    List<Load> findByShipperId(String shipperId);
    
    /**
     * Find loads by truck type.
     *
     * @param truckType the truck type
     * @return list of loads
     */
    List<Load> findByTruckType(String truckType);
    
    /**
     * Find loads by status.
     *
     * @param status the load status
     * @return list of loads
     */
    List<Load> findByStatus(LoadStatus status);
    
    /**
     * Find loads by shipper ID and status.
     *
     * @param shipperId the shipper ID
     * @param status the load status
     * @return list of loads
     */
    List<Load> findByShipperIdAndStatus(String shipperId, LoadStatus status);
}
