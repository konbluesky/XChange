package org.knowm.xchange.gateio.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class PrefixTrieContainer {
    private final Multimap<String, String> trie;

    public PrefixTrieContainer() {
        this.trie = HashMultimap.create();
    }

    // 添加元素到Trie容器
    public void addElement(String element) {
        // 将元素以所有可能的前缀添加到Multimap中
        for (int i = 0; i <= element.length(); i++) {
            String prefix = element.substring(0, i);
            trie.put(prefix, element);
        }
    }

    // 按照前缀检索元素
    public Iterable<String> getByPrefix(String prefix) {
        return trie.get(prefix);
    }

    public static void main(String[] args) {
        PrefixTrieContainer container = new PrefixTrieContainer();

        // 添加一些元素
        container.addElement("apple");
        container.addElement("banana");
        container.addElement("app");
        container.addElement("bat");
        container.addElement("ball");

        // 通过前缀检索元素
        Iterable<String> result = container.getByPrefix("ba");
        // 输出检索结果
        System.out.println("Elements with prefix 'ba': " + result);

        result = container.getByPrefix("app");

        System.out.println("Elements with prefix 'app': " + result);

    }
}
