package com.myself.server.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 角色集合反序列化类
 *
 * @author Wei
 * @since 2021/6/5
 */
public class AdminAuthorityDeserializer extends JsonDeserializer {

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        //读取json树
        JsonNode jsonNode = mapper.readTree(jsonParser);
        //获取json元素
        Iterator<JsonNode> elements = jsonNode.elements();
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        //迭代元素
        while (elements.hasNext()){
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.asText());
            grantedAuthorities.add(simpleGrantedAuthority);
        }
        return grantedAuthorities;
    }
}
