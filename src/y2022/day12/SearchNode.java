package y2022.day12;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import utils.Point;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Node in a graph, where the node has its adjacency list
 */
public class SearchNode {
    private Point position;  // This identifies it in the height map for ease of debugging later
    private Map<SearchNode, Integer> adjacentNodes = Maps.newHashMap();  // Adjacency list, with edge weight

    private List<SearchNode> shortestPath = Lists.newLinkedList();  // Short path to this node from source, used in our search algorithm
    private Integer weightFromSource = Integer.MAX_VALUE;  // Initialize max distance because we haven't performed the search yet

    public void addAdjacency(SearchNode destination, Integer edgeWeight) {
        adjacentNodes.put(destination, edgeWeight);
    }

    public SearchNode(Point position) {
        this.position = position;
    }

    // Only meaningful if we've already done the search and updated shortest paths
    public Integer getShorestPathWeight() {
        return weightFromSource;
    }

    public void setWeightFromSource(Integer weight) {
        weightFromSource = weight;
    }

    public Map<SearchNode, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public List<SearchNode> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<SearchNode> path) {
        shortestPath = path;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    // Only use the position for node equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchNode that = (SearchNode) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
