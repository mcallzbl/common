package com.mcallzbl.user.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcallzbl.user.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Spring Security用户详情实现类
 * 包装User实体类为Spring Security所需的UserDetails接口
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    /**
     * -- GETTER --
     *  获取用户实体
     */
    private User user;

    /**
     * 获取用户权限
     * 这里暂时返回空权限，后续可以根据用户角色动态加载
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根据用户的角色和权限生成权限列表
        // 这里可以扩展为从数据库查询用户权限
        if (user != null) {
            // 示例：根据用户状态返回相应权限
            Collection<GrantedAuthority> authorities = Collections.emptyList();

            // 可以根据用户的角色或权限表查询实际权限
            // 例如：return user.getRoles().stream()
            //     .flatMap(role -> role.getPermissions().stream())
            //     .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
            //     .collect(Collectors.toList());

            return authorities;
        }
        return Collections.emptyList();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user != null ? user.getPasswordHash() : null;
    }

    @Override
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true; // 账户是否未过期
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true; // 账户是否未锁定
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true; // 凭证是否未过期
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user != null && !user.isInActive(); // 用户是否启用
    }

    /**
     * 获取用户ID
     */
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

}