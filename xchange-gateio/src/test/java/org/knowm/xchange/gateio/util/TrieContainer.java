package org.knowm.xchange.gateio.util;


import java.util.ArrayList;
import java.util.List;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */

public class TrieContainer {

  private TrieNode root;

  public TrieContainer() {
    this.root = new TrieNode();
  }

  public static void main(String[] args) {
    TrieContainer trieContainer = new TrieContainer();
    trieContainer.put("apple");
    trieContainer.put("app");
    trieContainer.put("banana");
    trieContainer.put("bat");

    String prefix = "ba";
    List<String> matchingElements = trieContainer.getAllMatchingElements(prefix);

    System.out.println("Words matching prefix '" + prefix + "': " + matchingElements);
    // 输出: Words matching prefix 'app': [app, apple]
  }

  public void put(String word) {
    TrieNode node = root;
    for (char c : word.toCharArray()) {
      node.children.computeIfAbsent(c, k -> new TrieNode());
      node = node.children.get(c);
    }
    node.isEndOfWord = true;
  }

  public boolean search(String word) {
    TrieNode node = getNode(word);
    return node != null && node.isEndOfWord;
  }

  public boolean hasStartsWith(String prefix) {
    return getNode(prefix) != null;
  }

  public List<String> getAllMatchingElements(String prefix) {
    List<String> result = new ArrayList<>();
    TrieNode node = getNode(prefix);
    if (node != null) {
      collectMatchingElements(node, prefix, result);
    }
    return result;
  }

  private void collectMatchingElements(TrieNode node, String currentPrefix, List<String> result) {
    if (node.isEndOfWord) {
      result.add(currentPrefix);
    }

    for (char c : node.children.keySet()) {
      collectMatchingElements(node.children.get(c), currentPrefix + c, result);
    }
  }

  private TrieNode getNode(String prefix) {
    TrieNode node = root;
    for (char c : prefix.toCharArray()) {
      node = node.children.get(c);
      if (node == null) {
        return null;
      }
    }
    return node;
  }

  private static class TrieNode {

    private final java.util.Map<Character, TrieNode> children = new java.util.HashMap<>();
    private boolean isEndOfWord;

    public TrieNode() {
      this.isEndOfWord = false;
    }
  }
}
