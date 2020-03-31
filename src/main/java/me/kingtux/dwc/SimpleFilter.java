package me.kingtux.dwc;

import com.kennycason.kumo.nlp.filter.Filter;

import java.security.Permission;
import java.util.List;

public class SimpleFilter extends Filter {
    private List<String> bannedwords;


    public SimpleFilter(List<String> guildBannedWords) {
        this.bannedwords = guildBannedWords;
    }

    @Override
    public boolean test(String s) {
        return bannedwords.stream().noneMatch(s::equalsIgnoreCase);
    }
}
