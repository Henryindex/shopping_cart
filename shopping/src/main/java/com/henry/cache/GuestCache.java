package com.henry.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class GuestCache {

    private final Map<String, Integer> guestIdMap = new HashMap<>();

    public void setGuestIdMap(String guestId, Integer cartId){
        guestIdMap.put(guestId, cartId);
    }

    public Integer getGuestCartId(String guestId){
        return guestIdMap.get(guestId);
    }

    public void removeCartId(String guestId){
        guestIdMap.remove(guestId);
    }
}
