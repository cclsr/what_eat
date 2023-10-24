package com.cclsr.eat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.User;
import com.cclsr.eat.service.UserService;
import com.cclsr.eat.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user) {
        // 获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // 生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("签名","",phone,code);
//          短信服务没申请下来 这步跳过
            log.info("生成的验证码是：{}", code);
            // 需要将生成的验证码保存到redis 有效期5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("手机验证码短信发送失败");
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session) {
        // 获取手机号
        String phone = user.get("phone").toString();
        // 获取验证码
        String code = user.get("code").toString();
        // 从session中获取保存的验证码
        //String sessionCode = session.getAttribute(phone).toString();
        // 从redis中获取验证码
        String redisCode = (String) redisTemplate.opsForValue().get(phone);
        // 进行验证码的比对
        if (redisCode != null && redisCode.equals(code)) {
            // 比对成功 判断数据库是否存在 不存在进行新增
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User userInfo = userService.getOne(queryWrapper);
            if (userInfo == null) {
                userInfo = new User();
                userInfo.setPhone(phone);
                userService.save(userInfo);
            }
            session.setAttribute("user", userInfo.getId());
            // 如果登录成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);
            return R.success(userInfo);
        }
        return R.error("用户登录失败");
    }
}
