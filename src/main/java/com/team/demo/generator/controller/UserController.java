package com.team.demo.generator.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.demo.config.Result;
import com.team.demo.generator.dao.UserMapper;
import com.team.demo.generator.entity.User;
import com.team.demo.generator.service.UserService;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ky
 * @since 2024年04月08日
 */
@RestController
@RequestMapping("/user")
public class UserController {

    static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<>());

    @Resource
    UserMapper userMapper;

    @Resource
    UserService userService;

    @GetMapping("/")
    public List<User> getUserList() {
        List<User> r = new ArrayList<>(users.values());
        return r;
    }


    @PostMapping("/")
    public String postUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return "success";
    }


    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {

        User user = userMapper.findById(id);
        return user;
    }

    @PostMapping
    public Result<?> save(@RequestBody User user)
    {
        userMapper.insert(user);
        return Result.success();
    }

    @GetMapping("distance/{id}")
    public Page<User> getPager(@PathVariable Integer id) {

        return userMapper.findP(id);
    }

    @GetMapping("/find")
    public List<User> find()
    {
        List<Integer> userL = new ArrayList<>();
        userL.add(1);
        userL.add(2);
        userL.add(3);
        userL.add(4);
        userL.add(5);
        List<User> user = userMapper.queryUserByIds(userL);
        System.out.println("user = " + user);
        return user;
    }


    @GetMapping
    public Result<?> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestParam String search)
    {
        userMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<User>lambdaQuery().like(User::getUsername,search));
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        //业务逻辑: 根据u/p查询数据库 true: token false null
        String token = userService.login(user);
        if(token == null){
            //表示后端查询失败,返回用户201
            return Result.error("101","密码错误");
        }   //表示有数据,返回值为200
        return Result.success(token);
    }
}

