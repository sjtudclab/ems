package try_web;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public Map<String,Object> greeting(@RequestParam(value = "name") String name) {
		
		String resource="conf.xml";
		InputStream is=GreetingController.class.getClassLoader().getResourceAsStream(resource);
		SqlSessionFactory sessionFactory=new SqlSessionFactoryBuilder().build(is);
		SqlSession session=sessionFactory.openSession();
		String statement="sqlMapping.userMapper.getUser";
		User user=session.selectOne(statement, name);
		int uid=user.getUid();
		System.out.println(uid);
		String statement2="sqlMapping.can_sub_Mapper.getCanSub";
		can_sub cansub=session.selectOne(statement2, uid);
		int sid=cansub.getSubjectId();
		System.out.println(sid);
		String statement1="sqlMapping.subjectMapper.getSubject";
		Subject subject=session.selectOne(statement1, sid);
		System.out.println(subject);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name", user.getUname());
		map.put("id", user.getUid());
		map.put("cid", user.getCid());
		map.put("subject",subject.getName());
		map.put("time", subject.getDate());
		return map;
	}
	
}
