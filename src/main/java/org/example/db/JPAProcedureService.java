package org.example.db;

import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.example.exception.JpaException;

import java.util.Map;

import static java.lang.String.format;

/**
 * Service for getting/executing a stored procedure in the database.
 * <p>
 * <b> Note: Before using, make sure that the JPAService is
 * {@link JPAService#initialize initialized} </b>
 * </p>
 */
public class JPAProcedureService {

    private static JPAService jpaService;

    static {
        jpaService = JPAService.getInstance();
    }

    private JPAProcedureService() {
    }

    public static StoredProcedureQuery getStoredProcedureQuery(String procedureName) {
        try {
            return jpaService.getEntityManager().createStoredProcedureQuery(procedureName);
        } catch (IllegalArgumentException e) {
            throw new JpaException(format("Procedure of the given name '%s' may not exist.", procedureName), e);
        }
    }

    public static StoredProcedureQuery executeStoredProcedureQuery(String procedureName,
                                                                   Map<String, Object> incomingParams, Map<String, Object> outcomingParams, Map<String, Object> inOutParams) {
        final StoredProcedureQuery procedureQuery = getStoredProcedureQuery(procedureName);
        if (MapUtils.isNotEmpty(incomingParams))
            registerParameter(procedureQuery, incomingParams, ParameterMode.IN);

        if (MapUtils.isNotEmpty(outcomingParams))
            registerParameter(procedureQuery, outcomingParams, ParameterMode.OUT);

        if (MapUtils.isNotEmpty(inOutParams))
            registerParameter(procedureQuery, inOutParams, ParameterMode.INOUT);

        try {

            procedureQuery.execute();
            return procedureQuery;

        } catch (PersistenceException e) {
            throw new JpaException(format("Procedure '%s' Persistence Exception.", procedureName), e);
        }
    }

    private static void registerParameter(StoredProcedureQuery procedure, Map<String, Object> params,
                                          ParameterMode mode) {
        params.forEach((parameterName, value) -> {

            if (ClassUtils.isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
                procedure.registerStoredProcedureParameter(parameterName, value.getClass(), mode);
                procedure.setParameter(parameterName, value);
            } else
                throw new JpaException(
                        format("Incoming parameter '%s' not suitable. Use the @NamedNativeQueries approach instead",
                                parameterName));
        });
    }

    public static StoredProcedureQuery executeStoredProcedureQuery(String procedureName) {
        return executeStoredProcedureQuery(procedureName, null, null, null);
    }

    public static StoredProcedureQuery executeStoredProcedureQuery(String procedureName,
                                                                   Map<String, Object> incomingParams) {
        return executeStoredProcedureQuery(procedureName, incomingParams, null, null);
    }

    public static StoredProcedureQuery executeStoredProcedureQuery(String procedureName,
                                                                   Map<String, Object> incomingParams, Map<String, Object> outcomingParams) {
        return executeStoredProcedureQuery(procedureName, incomingParams, outcomingParams, null);
    }

}
