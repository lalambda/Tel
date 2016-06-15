package com.example.admin.tel;

import java.util.List;
import java.util.Map;

/**
 * Created by yaoyafeng on 16/6/7.
 */
public class PinYinName {
    private List<Map<String,Integer>> spellAll;

    private String spellHead;

    public String getSpellHead() {
        return spellHead;
    }

    public void setSpellHead(String spellHead) {
        this.spellHead = spellHead;
    }

    public List<Map<String, Integer>> getSpellAll() {
        return spellAll;
    }

    public void setSpellAll(List<Map<String, Integer>> spellAll) {
        this.spellAll = spellAll;
    }

}
