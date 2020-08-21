package com.mepeng.cn.SevenPen.order.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.gson.JsonObject;
import com.mepeng.cn.SevenPen.order.configuration.RedisMsg;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation(value = "根据key(id)查询")
    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public Object getById(@PathVariable("key") String key){
        LOGGER.info("根据key {} 进行查询", key);
        Person person = (Person)redisTemplate.opsForValue().get(key);
        return person;
    }

    @ApiOperation("用id做key，保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody Person person){
        LOGGER.info("调用保存接口Person:{}", person);
        //不存在就保存 存在就更新
        redisTemplate.opsForValue().set(person.getId(), person);
    }

    @ApiOperation("删除")
    @ApiImplicitParam(name = "key", value = "用户id")
    @RequestMapping(value = "/{key}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("key") String key){
        LOGGER.info("调用删除接口");
        // 不存在就返回false
        return  redisTemplate.delete(key);
    }

    @ApiOperation("更新")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Object update(@RequestBody Person person){
        LOGGER.info("调用更新接口Person:{}", person);
        //得到旧值   不存在就保存 存在就更新
        Object andSet = redisTemplate.opsForValue().getAndSet(person.getId(), person);
        LOGGER.info("调用更新接口Person:{},旧值andSet:{}", person,andSet);
        return andSet;
    }

    //set和list简单操作
    @ApiOperation(value = "多个value,分割，保存到set中")
    @RequestMapping(value = "/saveToSet/{key}", method = RequestMethod.POST)
    public Long save(@PathVariable("key") String key, @RequestParam String values){
        LOGGER.info("保存values：{}", values);
        // set保存，返回保存长度；重复项-1
        return redisTemplate.opsForSet().add(key, values.split(","));
    }

    @ApiOperation(value = "将set中的值，移动到list中")
    @RequestMapping(value = "/moveToList", method = RequestMethod.POST)
    public long move(@RequestParam String setKey, @RequestParam String listKey){
        LOGGER.info("将set{}中的值，移动到list{}中：", setKey, listKey);
        Set members = redisTemplate.opsForSet().members(setKey);
        LOGGER.info("将要copy的set长度为{}，values{}", redisTemplate.opsForSet().size(setKey), members.toArray());
        // 返回list长度
        return redisTemplate.opsForList().leftPushAll(listKey, members);
    }

    @ApiOperation(value = "从list弹出一个值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", value = "方向", dataType = "String", paramType = "query",
                    allowableValues = "left,right")
    })
    @RequestMapping(value = "/listPop", method = RequestMethod.GET)
    public Object lkstPop(@RequestParam String listKey, @RequestParam String direction) {
        //从redis list中弹出一个值 list中将不再有这个值
        if (direction.equalsIgnoreCase("right")) {
            return redisTemplate.opsForList().rightPop(listKey);
        }
        return redisTemplate.opsForList().leftPop(listKey);
    }

    //加锁和续期：加锁使用测试2；续期使用测试3
    @ApiOperation("测试1")
    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public Boolean test1(@RequestBody Person person){
        LOGGER.info("测试1 person.name:{}", person.getName());
        // 设置过期时间 10秒/ 不存在返回false
        Boolean result = redisTemplate.expire(person.getName(), 10, TimeUnit.SECONDS);
        LOGGER.info("测试1 person.name:{},result:{}", person.getName(),result);
        return result;
    }

    @ApiOperation("加锁 测试2，不存在key就新建并设置过期时间")
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public Boolean test2(@RequestBody Person person){
        LOGGER.info("测试2，不存在key就新建并设置过期时间 person.name:{}", person.getName());
        Boolean result = redisTemplate.opsForValue().setIfAbsent(person.getName(), person, 10, TimeUnit.SECONDS);
        LOGGER.info("测试2，不存在key就新建并设置过期时间 person.name:{},result:{}", person.getName(),result);
        return result;
    }

    /**
     * 续期优化：可以在事务中check锁是你的锁，然后再续期，不要瞎续期；或者用redis lua脚本
     *
     * 解锁优化：同理检查锁是你的锁
     * @param person
     * @return
     */
    @ApiOperation("加锁 测试2，不存在key就新建并设置过期时间")
    @RequestMapping(value = "/test21", method = RequestMethod.GET)
    public Boolean test21(@RequestBody Person person){
        System.out.println(Thread.currentThread().getId()+"is doing..=========");
        LOGGER.info("测试2，不存在key:{},就新建并设置永不过期 person.name:{}", Thread.currentThread().getId(),person.getName());
        Boolean result = redisTemplate.opsForValue().setIfAbsent(person.getName(), person, -1, TimeUnit.SECONDS);
        while(!result){//存在key result = false 直到不存在key result=true 设置key永不过期
            try {
                Thread.sleep(100L);
                //System.out.println(Thread.currentThread().getId()+"获取不到锁，睡眠100毫秒后重新拿锁");
                result = redisTemplate.opsForValue().setIfAbsent(person.getName(), person, -1, TimeUnit.SECONDS);
                if(result){
                    System.out.println("key:"+person.getName()+",tid:"+Thread.currentThread().getId()+"获取不到锁，睡眠100毫秒后重新拿锁result："+result);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("key:"+person.getName()+",tid:"+Thread.currentThread().getId()+"获取到分布式锁，接下来可以确保只有一个线程运行");
        try {
            Thread.sleep(30000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("key:"+person.getName()+",tid:"+Thread.currentThread().getId()+"获取到分布式锁，一个线程运行处理业务完成，把分布式锁KEY 过期时间设置0S已过期");
        result = redisTemplate.expire(person.getName(), 0, TimeUnit.SECONDS);
        System.out.println("key:"+person.getName()+",tid:"+Thread.currentThread().getId()+"获取到分布式锁，一个线程运行处理业务完成，把分布式锁KEY 过期时间设置0S已过期 已释放锁");
        LOGGER.info("key:"+person.getName()+",tid:"+Thread.currentThread().getId()+"测试2，不存在key就新建并设置过期时间 person.name:{},result:{}", person.getName(),result);
        return result;
    }

    @ApiOperation("续期 测试3,存在就更新并设置过期时间,存在返回true，不存在返回false")
    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    public Boolean test3(@RequestBody Person person){
        LOGGER.info("测试3,存在就更新并设置过期时间,存在返回true，不存在返回false person.name:{}", person.getName());
        Boolean result = redisTemplate.opsForValue().setIfPresent(person.getName(), person, 10, TimeUnit.SECONDS);
        LOGGER.info("测试3,存在就更新并设置过期时间,存在返回true，不存在返回false person.name:{},result:{}", person.getName(),result);
        return result;
    }


    //测试发送redis 广播 redisUtil.convertAndSend(RedisMsg.channel,jsonObject.toJSONString());
    @ApiOperation("测试发送redis 广播")
    @RequestMapping(value = "/convertAndSend", method = RequestMethod.POST)
    public Boolean convertAndSend(@RequestBody Person person){
        LOGGER.info("测试发送redis 广播 person.name:{}", person.getName());
        Boolean result = false;
        try {

            Object message = person;
            JSONPObject jsonP = new JSONPObject();
            message = JSON.toJSONString(person);

            String personJson = JSONObject.toJSONString(person);
            System.out.println("发送redis personJson:"+personJson);
            redisTemplate.convertAndSend(RedisMsg.channel,personJson);
            result =  true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        LOGGER.info("测试发送redis 广播 person.name:{},result:{}", person.getName(),result);
        return result;
    }

    @ApiOperation("测试发送redis 广播2")
    @RequestMapping(value = "/convertAndSend2", method = RequestMethod.POST)
    public Boolean convertAndSend2(@RequestBody Person person){
        LOGGER.info("convertAndSend2 测试发送redis 广播 person.name:{}", person.getName());
        Boolean result = false;
        try {

            Object message = person;
            JSONPObject jsonP = new JSONPObject();
            message = JSON.toJSONString(person);

            String personJson = JSONObject.toJSONString(person);
            System.out.println("convertAndSend2 发送redis personJson:"+personJson);
            redisTemplate.convertAndSend(RedisMsg.channel2,personJson);
            result =  true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        LOGGER.info("convertAndSend2 测试发送redis 广播 person.name:{},result:{}", person.getName(),result);
        return result;
    }

    public static void main(String[] args) {
        Person person = new Person();
        person.setId("234");
        person.setName("csf");
        person.setCrtDate(new Date());
        String personJson = JSONObject.toJSONString(person);
        System.out.println("personJson:"+personJson);
        JSONObject json = JSONObject.parseObject(personJson);
        Person person1 = JSON.toJavaObject(json,Person.class);
        System.out.println("obj person.id="+person1.getId());
        System.out.println("obj person.name="+person1.getName());
        System.out.println("obj person.crtDate="+person1.getCrtDate());

        String str1 = "{\\\"crtDate\\\":1597744232000,\"id\":\"123\",\"name\":\"test32333qwe\"}";
        System.out.println("str1:"+str1);

        System.out.println("str1:"+str1.replaceAll("\\\\\"","\""));
    }
}
