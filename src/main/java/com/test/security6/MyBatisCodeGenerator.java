package com.test.security6;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

public class MyBatisCodeGenerator {
    public static void main(String[] args){
        FastAutoGenerator.create("jdbc:mysql://192.168.58.134:3308/security_test_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","alice","mysql!docker@alice&3308")
                .globalConfig(builder -> {
                    builder.author("alice")
                            .outputDir(System.getProperty("user.dir")+"/src/main/java")
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent("com.scenery.securitytest")
                            .entity("entity.db");
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                }).execute();
    }
}
