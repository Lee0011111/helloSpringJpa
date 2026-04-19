package kr.ac.hansung.cse.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.ac.hansung.cse.model.Category;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public Optional<Category> findByName(String name) {
        List<Category> r = em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name).getResultList();
        return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
    }

    public long countProductsByCategoryId(Long categoryId) {
        return em.createQuery("SELECT COUNT(p) FROM Product p WHERE p.category.id = :id", Long.class)
                .setParameter("id", categoryId).getSingleResult();
    }

    public Category save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
            return category;
        } else {
            return em.merge(category);
        }
    }

    public void delete(Long id) {
        Category c = em.find(Category.class, id);
        if (c != null) em.remove(c);
    }
}