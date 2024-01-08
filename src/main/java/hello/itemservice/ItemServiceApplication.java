package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * # spring boot 사용 x : 수동등록
 *   ex) new AnnotationConfigApplicationContext(AppConfig.class);
 *    1. ApplicatonContext(스프링 컨테이너) 생성
 *    2. AppConfig를 스프링 빈으로 등록
 *    3. AppConfig에 @Configuration이 달려있고, @Bean 애노테이션이 있으면 달려있는 컴포넌트를 컨테이너에 빈으로 등록
 *    	=> 같은 설정파일에서 의존관계를 '직접' 주입
 *
 *
 * # spring boot 사용 : 자동등록
 *    1. @SpringBootApplication 이 붙어있는 class(이 예제에서는 CoreApplication)의 main 함수가 실행.
 *    2. 스프링부트 내부에서 자동으로 ApplicationContext (스프링 컨테이너)를 생성.
 *    3. @SpringBootApplication에는 @ComponentScan이 포함되어 있음.
 *    4. @Component 어노테이션이 달려있는 클래스를 스프링 컨테이너에 빈으로 등록(controller, service, repository...)
 *       => @RequiredArgsConstructor, @Autowired등을 통해 의존관계를 '자동' 주입
 *
 *       (@Configuration에도 @Component가 포함되어있으므로 이 어노테이션이 달려있는 설정 클래스도 빈으로 컨테이너에 등록,
 *        이때 만약 @Bean 어노테이션이 있으면 빈을 컨테이너에 등록)
 */

//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
@Import(JdbcTemplateV3Config.class)
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

}
