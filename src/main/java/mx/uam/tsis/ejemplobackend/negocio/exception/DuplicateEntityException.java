package mx.uam.tsis.ejemplobackend.negocio.exception;

/**
 * Exception thrown when an entity already exists or would create a duplicate
 */
public class DuplicateEntityException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    private final String entityType;
    private final String identifier;
    private final String context;
    
    public DuplicateEntityException(String entityType, String identifier, String context) {
        super("Entity " + entityType + " with identifier " + identifier + " already exists in " + context);
        this.entityType = entityType;
        this.identifier = identifier;
        this.context = context;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public String getContext() {
        return context;
    }
} 