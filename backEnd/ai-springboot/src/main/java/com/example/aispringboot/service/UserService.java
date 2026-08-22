package com.example.aispringboot.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.aispringboot.DTO.command.UserLoginCommandDTO;
import com.example.aispringboot.DTO.command.UserRegisterCommandDTO;
import com.example.aispringboot.DTO.response.UserLoginResponseDTO;
import com.example.aispringboot.common.Result;
import com.example.aispringboot.entity.User;
import com.example.aispringboot.enumClass.UserType;
import com.example.aispringboot.exception.BusinessException;
import com.example.aispringboot.mapper.UserMapper;
import com.example.aispringboot.service.convert.UserConvert;
import com.example.aispringboot.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

    public UserLoginResponseDTO login(UserLoginCommandDTO commandDTO){
        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,commandDTO.getUsername()).or().eq(User::getEmail,commandDTO.getUsername());

        // 调用mybatis plus API进行查询
        User user = userMapper.selectOne(queryWrapper);

        // 判断用户是否存在
        if (user == null){
            throw new BusinessException("用户不存在");
        }

        // 验证密码
        String inputPassword = commandDTO.getPassword().trim();
        if (!passwordEncoder.matches(inputPassword,user.getPassword())){
            throw new BusinessException("密码错误");
        }

        // 检查用户状态
        if (!user.isActive()){
            throw new BusinessException("用户被禁用 请联系管理员");
        }

        // 生成JWT token
        String token = JwtTokenUtil.generateToken(user.getId(),user.getUsername(),user.getUserType());
        UserLoginResponseDTO.UserDetailResponseDTO userInfo = UserConvert.entityToDetailResponse(user);
        return UserConvert.entityToLoginResponse(token,userInfo);
    }

    public UserLoginResponseDTO.UserDetailResponseDTO register(UserRegisterCommandDTO commandDTO){
        System.out.println(JSONUtil.parseObj(commandDTO));
        // 验证密码是否一致
        if (!commandDTO.getPassword().equals(commandDTO.getConfirmPassword())){
            throw new BusinessException("两次密码输入不一致");
        }

        // 检查用户名是否存在
        LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
        usernameQuery.eq(User::getUsername,commandDTO.getUsername());

        if (userMapper.selectCount(usernameQuery) > 0) {
            throw new BusinessException("用户已存在");
        }

        // 检查邮箱
        LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
        emailQuery.eq(User::getEmail,commandDTO.getUsername());

        if (userMapper.selectCount(emailQuery) > 0) {
            throw new BusinessException("邮箱已存在");
        }

        // 检查用户类型（因为1为普通用户，2是管理员， 注册的必须是普通用户)
        if (!UserType.isValidCode(commandDTO.getUserType())){
            throw new BusinessException(("用户类型错误"));
        }

        // 创建用户
        String encodedPassword = passwordEncoder.encode(commandDTO.getPassword().trim());
        User user = UserConvert.registerCommandToEntity(commandDTO,encodedPassword);

        userMapper.insert(user);
        return UserConvert.entityToDetailResponse(user);
    }
}
