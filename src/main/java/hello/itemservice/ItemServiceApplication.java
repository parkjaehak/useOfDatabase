package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * # spring boot 사용 x : 수동등록
 * ex) new AnnotationConfigApplicationContext(AppConfig.class);
 * 1. ApplicatonContext(스프링 컨테이너) 생성
 * 2. AppConfig를 스프링 빈으로 등록
 * 3. AppConfig에 @Configuration이 달려있고, @Bean 애노테이션이 있으면 달려있는 컴포넌트를 컨테이너에 빈으로 등록
 * => 같은 설정파일에서 의존관계를 '직접' 주입
 *
 *
 * # spring boot 사용 : 자동등록
 * 1. @SpringBootApplication 이 붙어있는 class(이 예제에서는 CoreApplication)의 main 함수가 실행.
 * 2. 스프링부트 내부에서 자동으로 ApplicationContext (스프링 컨테이너)를 생성.
 * 3. @SpringBootApplication에는 @ComponentScan이 포함되어 있음.
 * 4. @Component 어노테이션이 달려있는 클래스를 스프링 컨테이너에 빈으로 등록(controller, service, repository...)
 * => @RequiredArgsConstructor, @Autowired등을 통해 의존관계를 '자동' 주입
 *
 * =================== 중요 ===================
 * # @Autowired, @RequiredArgsConstructor의 관계
 *
 * 생성자가 오직 하나만 있고, 생성자의 파라미터 타입이 빈으로 등록가능한 존재라면,
 * 이 빈은 @Autowired 어노테이션을 생략해도 @Autowired가 붙은 것과 동일하게 자동주입이 동작
 *
 * @RequiredArgsConstructor는 파라미터를 가지는 생성자를 생성해주는 역할
 *
 * 따라서 생성자가 하나인 경우 Autowired 생략 후 RequiredArgsConstructor 만 생성해도 자동주입이 되는 것임
 */

@Slf4j
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
@Import(SpringDataJpaConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

	/**
	 * DB를 애플리케이션 실행시 메모리에 내장해서 함께 실행한다고 해서
	 * 임베디드 모드(Embedded mode)라 한다
	 * 즉, jvm 메모리로 동작하는 DB
	 */
/*	@Bean
	@Profile("test")
	public DataSource dataSource() {
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"); // jvm 안에 db 생성 후 데이터 저장
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}*/
}
