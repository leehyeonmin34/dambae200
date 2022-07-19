package com.dambae200.dambae200.global.common;

import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Component
public class RepoUtils {

//    final private ToEntity toEntity;

//    final private ToDto toDto;

    public <R extends JpaRepository<E, Long>, E extends BaseEntity> E getOneElseThrowException(R repository, Long id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no entity found by" + repository.getClass().getName().toString() + " from id " + id));
    }

    public <R extends JpaRepository<E, Long>, E extends BaseEntity> void deleteOneElseThrowException(R repository, Long id) throws EntityNotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("no entity found by" + repository.getClass().getName() + " for id : " + id);
        }
    }
//    public <R extends JpaRepository<E, Long>, E extends BaseEntity> void validateDuplicate(R repository, Long id) throws DuplicateEntityException{
//        if(!repository.existsById(id)){
//            throw new EntityNotFoundException("duplicate entity found by" + repository.getClass().getName() + " for id : " + id);
//        }
//    }
//
//    public <R extends JpaRepository<E, Long>, E extends BaseEntity> void validateExist(R repository, Long id) throws DuplicateEntityException{
//        if(repository.existsById(id)){
//            throw new EntityNotFoundException("not existing entity searched by" + repository.getClass().getName() + " for id : " + id);
//        }
    }
