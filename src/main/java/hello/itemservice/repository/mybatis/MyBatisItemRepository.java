package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
//@Repository
/* JdbcTemplate는 스프링 내부적으로, MyBatis는 스프링 연동 모듈이 만들어주는 매퍼 구현체가 예외변환을 처리해준다.

 따라서 해당 기능을 꼭 사용하지 않아도 된다.
 하지만 JPA의 경우에는 @Repository가 예외변환을 처리해준다.*/

@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {

    // MyBatis-Spring Mapper 모듈이 만든 동적 프록시 객체를 의존관계 주입
    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {
        log.info("itemMapper class={}", itemMapper.getClass());
        itemMapper.save(item);
        return item;
    }

    @Override
    public void update(Long id, ItemUpdateDto itemUpdateDto) {
        itemMapper.update(id, itemUpdateDto);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return itemMapper.findAll(cond);
    }
}
