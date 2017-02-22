package org.jsicotte.challenge.conf;


import io.dropwizard.Configuration;

/**
 * App configuration
 */
public class AppConfiguration extends Configuration {
    private String sqliteUrl = "jdbc:sqlite:/Users/jsicotte/Documents/workspaces/programmingtest2/user";
    private String redisHost = "localhost";

    public String getSqliteUrl() {
        return sqliteUrl;
    }

    public void setSqliteUrl(String sqliteUrl) {
        this.sqliteUrl = sqliteUrl;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }
}
