package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Jpql 사용 이유
 * 엔티티 객체와 필드를 대상으로 쿼리를 작성하여 여기에 의존
 * 따라서 다양한 데이터베이스 및 ORM 프로바이더에서 동일한 쿼리를 실행할 수 있음
 */
@Slf4j
@Repository
@Transactional //Jpa에서 데이터를 변경할떄는 항상 transactioanl필요
public class JpaItemRepository implements ItemRepository {

  /* EntityManager가 Jpa임
     transactionManager, dataSource는 스프링부트가 알아서 설정해서 넘겨줌*/
    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long id, ItemUpdateDto itemUpdateDto) {
        Item findItem = em.find(Item.class, id);
        findItem.setItemName(itemUpdateDto.getItemName());
        findItem.setPrice(itemUpdateDto.getPrice());
        findItem.setQuantity(itemUpdateDto.getQuantity());
        // commit 되는 시점에 update query를 만들어 db에 저장
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        // jpql은 table 대상이 아닌 Entity 대상으로 함
        String jpql = "select i from Item i";

        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')"; //jpql += " i.itemName like '%" + itemName + "%'"; 와 같은 의미
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }

        log.info("jpql={}", jpql);

        TypedQuery<Item> query = em.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        List<Item> result = query.getResultList();

//        List<Item> result = em.createQuery(jpql, Item.class)
//                .getResultList();
//
        return result;
    }
}
