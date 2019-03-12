package com.bth.lht.rest.missionController;

import com.bth.lht.entity.user.UserEO;
import com.bth.lht.entity.user.UserInfoEO;
import com.bth.lht.respose.base.BaseResponse;
import com.bth.lht.respose.base.OneResponse;
import com.bth.lht.rest.baseController.BaseController;
import com.bth.lht.service.user.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: lht
 * @description: 任务控制器
 * @author: Antony
 * @create: 2019-03-12 09:38
 **/
@RestController
@RequestMapping("/mission")
@Api("任务接口")
public class MissionController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
//    private  UserInfoEO userInfoEO;

    @GetMapping("hello/{id}")
    public BaseResponse hello(@PathVariable(name="id") int id, @RequestParam String name){
        System.out.println("执行添加操作");
     return successSave();
    }


    /**
     * 返回单个实体
     */
//    @GetMapping("getOne/{id}")
//    public OneResponse getOne(@PathVariable(name = "id") int id){
//
//    }
}