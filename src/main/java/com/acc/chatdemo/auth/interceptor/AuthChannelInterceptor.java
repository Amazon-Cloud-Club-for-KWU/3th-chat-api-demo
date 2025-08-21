package com.acc.chatdemo.auth.interceptor;

import com.acc.chatdemo.auth.JwtProvider;
import com.acc.chatdemo.auth.UserDetailServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                String userId = jwtProvider.parseUserIdFromAccessToken(token.substring(7)).toString();

                UserDetails userDetails = userDetailService.loadUserByUsername(userId);
                accessor.setUser(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            } else {
                throw new IllegalArgumentException("Invalid or missing Authorization header");
            }
        }
        return message;
    }
}
