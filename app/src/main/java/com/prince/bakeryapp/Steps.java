package com.prince.bakeryapp;

import java.util.ArrayList;
import java.util.List;

public class Steps {
    List<Integer> ids = new ArrayList<>();
    List<String> shortDescriptions = new ArrayList<>();
    List<String> descriptions = new ArrayList<>();
    List<String> urls = new ArrayList<>();

    public Steps(List<Integer> ids, List<String> shortDescriptions, List<String> descriptions, List<String> urls) {
        this.ids = ids;
        this.shortDescriptions = shortDescriptions;
        this.descriptions = descriptions;
        this.urls = urls;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<String> getShortDescriptions() {
        return shortDescriptions;
    }

    public void setShortDescriptions(List<String> shortDescriptions) {
        this.shortDescriptions = shortDescriptions;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
