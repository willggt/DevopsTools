package mx.uam.tsis.ejemplobackend.negocio.exception;

/**
 * Exception thrown when an entity cannot be found in the system
 */
public class EntityNotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    private final String entityType;
    private final String identifier;
    
    public EntityNotFoundException(String entityType, String identifier) {
        super("Entity " + entityType + " with identifier " + identifier + " not found");
        this.entityType = entityType;
        this.identifier = identifier;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getIdentifier() {
        return identifier;
    }
} 