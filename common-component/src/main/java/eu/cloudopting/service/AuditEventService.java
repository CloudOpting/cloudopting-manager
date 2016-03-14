package eu.cloudopting.service;

import eu.cloudopting.config.audit.AuditEventConverter;
import eu.cloudopting.domain.PersistentAuditEvent;
import eu.cloudopting.repository.PersistenceAuditEventRepository;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional
public class AuditEventService {

    @Inject
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Inject
    private AuditEventConverter auditEventConverter;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AuditEvent> findAll() {
        return auditEventConverter.convertToAuditEvent(persistenceAuditEventRepository.findAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
        List<PersistentAuditEvent> persistentAuditEvents =
            persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate);

        return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
    }
}
