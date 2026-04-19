package kr.ac.hansung.cse.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.ac.hansung.cse.model.Product;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.id ASC", Product.class).getResultList();
    }

    // ⭐ 상세 화면 에러를 없애는 핵심: 카테고리까지 한 번에 다 가져오기! ⭐
    public Optional<Product> findById(Long id) {
        List<Product> results = em.createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id", Product.class)
                .setParameter("id", id)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            em.persist(product);
            return product;
        } else {
            return em.merge(product);
        }
    }

    public void delete(Long id) {
        Product product = em.find(Product.class, id);
        if (product != null) em.remove(product);
    }

    public List<Product> findByNameContaining(String keyword) {
        return em.createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.name LIKE :keyword ORDER BY p.id ASC", Product.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    public List<Product> findByCategoryId(Long categoryId) {
        return em.createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.id = :cid ORDER BY p.id ASC", Product.class)
                .setParameter("cid", categoryId)
                .getResultList();
    }
}