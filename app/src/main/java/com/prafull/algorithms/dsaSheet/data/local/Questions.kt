package com.prafull.algorithms.dsaSheet.data.local


data class Topic(
    val topicName: String,
    val topicDetails: String,
    val questions: List<Question>
) {
    fun toTopicEntity(): TopicEntity {
        return TopicEntity(
            topicName = topicName,
            topicDetails = topicDetails
        )
    }
}

data class Question(
    val name: String,
    val link: String,
    val solved: Boolean = false,
    val revision: Boolean = false,
    val note: String = ""
) {
    fun toQuestionEntity(): QuestionEntity {
        return QuestionEntity(
            name = name,
            link = link,
            solved = solved,
            revision = revision,
            note = note
        )
    }
}

val leetcodeDSATopics = listOf(
    Topic(
        topicName = "Learn the Basics",
        topicDetails = "Fundamental problems to get started with DSA",
        questions = listOf(
            Question("Two Sum", "https://leetcode.com/problems/two-sum/"),
            Question("Reverse String", "https://leetcode.com/problems/reverse-string/"),
            Question("Fizz Buzz", "https://leetcode.com/problems/fizz-buzz/"),
            Question("Palindrome Number", "https://leetcode.com/problems/palindrome-number/"),
            Question("Roman to Integer", "https://leetcode.com/problems/roman-to-integer/"),
            Question("Maximum Subarray", "https://leetcode.com/problems/maximum-subarray/"),
            Question("Move Zeroes", "https://leetcode.com/problems/move-zeroes/"),
            Question("Valid Anagram", "https://leetcode.com/problems/valid-anagram/"),
            Question("Contains Duplicate", "https://leetcode.com/problems/contains-duplicate/"),
            Question("Single Number", "https://leetcode.com/problems/single-number/"),
            Question("Plus One", "https://leetcode.com/problems/plus-one/"),
            Question(
                "Intersection of Two Arrays II",
                "https://leetcode.com/problems/intersection-of-two-arrays-ii/"
            ),
            Question(
                "Best Time to Buy and Sell Stock",
                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/"
            ),
            Question("Valid Parentheses", "https://leetcode.com/problems/valid-parentheses/"),
            Question("Missing Number", "https://leetcode.com/problems/missing-number/")
        )
    ),
    Topic(
        topicName = "Learn Important Sorting Techniques",
        topicDetails = "Various sorting algorithms and their applications",
        questions = listOf(
            Question("Sort an Array", "https://leetcode.com/problems/sort-an-array/"),
            Question("Sort Colors", "https://leetcode.com/problems/sort-colors/"),
            Question("Insertion Sort List", "https://leetcode.com/problems/insertion-sort-list/"),
            Question("Merge Intervals", "https://leetcode.com/problems/merge-intervals/"),
            Question("Maximum Gap", "https://leetcode.com/problems/maximum-gap/"),
            Question("Largest Number", "https://leetcode.com/problems/largest-number/"),
            Question(
                "Kth Largest Element in an Array",
                "https://leetcode.com/problems/kth-largest-element-in-an-array/"
            ),
            Question(
                "Find the Kth Largest Integer in the Array",
                "https://leetcode.com/problems/find-the-kth-largest-integer-in-the-array/"
            ),
            Question(
                "Sort Characters By Frequency",
                "https://leetcode.com/problems/sort-characters-by-frequency/"
            ),
            Question("Sort List", "https://leetcode.com/problems/sort-list/"),
            Question("Pancake Sorting", "https://leetcode.com/problems/pancake-sorting/"),
            Question("Relative Sort Array", "https://leetcode.com/problems/relative-sort-array/"),
            Question("Custom Sort String", "https://leetcode.com/problems/custom-sort-string/"),
            Question(
                "Sort Array by Increasing Frequency",
                "https://leetcode.com/problems/sort-array-by-increasing-frequency/"
            ),
            Question("Wiggle Sort II", "https://leetcode.com/problems/wiggle-sort-ii/")
        )
    ),
    Topic(
        topicName = "Arrays",
        topicDetails = "Problems focused on array manipulation and algorithms",
        questions = listOf(
            Question("Two Sum", "https://leetcode.com/problems/two-sum/"),
            Question(
                "Best Time to Buy and Sell Stock",
                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/"
            ),
            Question("Contains Duplicate", "https://leetcode.com/problems/contains-duplicate/"),
            Question(
                "Product of Array Except Self",
                "https://leetcode.com/problems/product-of-array-except-self/"
            ),
            Question("Maximum Subarray", "https://leetcode.com/problems/maximum-subarray/"),
            Question(
                "Maximum Product Subarray",
                "https://leetcode.com/problems/maximum-product-subarray/"
            ),
            Question(
                "Find Minimum in Rotated Sorted Array",
                "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/"
            ),
            Question(
                "Search in Rotated Sorted Array",
                "https://leetcode.com/problems/search-in-rotated-sorted-array/"
            ),
            Question("3Sum", "https://leetcode.com/problems/3sum/"),
            Question(
                "Container With Most Water",
                "https://leetcode.com/problems/container-with-most-water/"
            ),
            Question(
                "Subarray Sum Equals K",
                "https://leetcode.com/problems/subarray-sum-equals-k/"
            ),
            Question("Next Permutation", "https://leetcode.com/problems/next-permutation/"),
            Question("Spiral Matrix", "https://leetcode.com/problems/spiral-matrix/"),
            Question("Rotate Image", "https://leetcode.com/problems/rotate-image/"),
            Question("Jump Game", "https://leetcode.com/problems/jump-game/")
        )
    ),
    Topic(
        topicName = "Binary Search",
        topicDetails = "Problems solved using binary search algorithm",
        questions = listOf(
            Question("Binary Search", "https://leetcode.com/problems/binary-search/"),
            Question(
                "Search Insert Position",
                "https://leetcode.com/problems/search-insert-position/"
            ),
            Question(
                "Search in Rotated Sorted Array",
                "https://leetcode.com/problems/search-in-rotated-sorted-array/"
            ),
            Question(
                "Find First and Last Position of Element in Sorted Array",
                "https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/"
            ),
            Question("Search a 2D Matrix", "https://leetcode.com/problems/search-a-2d-matrix/"),
            Question(
                "Find Minimum in Rotated Sorted Array",
                "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/"
            ),
            Question(
                "Peak Index in a Mountain Array",
                "https://leetcode.com/problems/peak-index-in-a-mountain-array/"
            ),
            Question("Find Peak Element", "https://leetcode.com/problems/find-peak-element/"),
            Question("Sqrt(x)", "https://leetcode.com/problems/sqrtx/"),
            Question(
                "Guess Number Higher or Lower",
                "https://leetcode.com/problems/guess-number-higher-or-lower/"
            ),
            Question(
                "Capacity To Ship Packages Within D Days",
                "https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/"
            ),
            Question("Koko Eating Bananas", "https://leetcode.com/problems/koko-eating-bananas/"),
            Question(
                "Median of Two Sorted Arrays",
                "https://leetcode.com/problems/median-of-two-sorted-arrays/"
            ),
            Question(
                "Find the Duplicate Number",
                "https://leetcode.com/problems/find-the-duplicate-number/"
            ),
            Question(
                "Split Array Largest Sum",
                "https://leetcode.com/problems/split-array-largest-sum/"
            )
        )
    ),
    Topic(
        topicName = "Strings",
        topicDetails = "Problems focused on string manipulation and algorithms",
        questions = listOf(
            Question("Reverse String", "https://leetcode.com/problems/reverse-string/"),
            Question("Valid Anagram", "https://leetcode.com/problems/valid-anagram/"),
            Question(
                "Longest Common Prefix",
                "https://leetcode.com/problems/longest-common-prefix/"
            ),
            Question("Valid Palindrome", "https://leetcode.com/problems/valid-palindrome/"),
            Question(
                "String to Integer (atoi)",
                "https://leetcode.com/problems/string-to-integer-atoi/"
            ),
            Question(
                "Longest Substring Without Repeating Characters",
                "https://leetcode.com/problems/longest-substring-without-repeating-characters/"
            ),
            Question(
                "Longest Palindromic Substring",
                "https://leetcode.com/problems/longest-palindromic-substring/"
            ),
            Question("Group Anagrams", "https://leetcode.com/problems/group-anagrams/"),
            Question("Valid Parentheses", "https://leetcode.com/problems/valid-parentheses/"),
            Question("Generate Parentheses", "https://leetcode.com/problems/generate-parentheses/"),
            Question("Implement strStr()", "https://leetcode.com/problems/implement-strstr/"),
            Question("Zigzag Conversion", "https://leetcode.com/problems/zigzag-conversion/"),
            Question(
                "Letter Combinations of a Phone Number",
                "https://leetcode.com/problems/letter-combinations-of-a-phone-number/"
            ),
            Question(
                "Longest Valid Parentheses",
                "https://leetcode.com/problems/longest-valid-parentheses/"
            ),
            Question(
                "Minimum Window Substring",
                "https://leetcode.com/problems/minimum-window-substring/"
            )
        )
    ),
    Topic(
        topicName = "Linked List",
        topicDetails = "Problems related to linked list data structure",
        questions = listOf(
            Question("Reverse Linked List", "https://leetcode.com/problems/reverse-linked-list/"),
            Question(
                "Merge Two Sorted Lists",
                "https://leetcode.com/problems/merge-two-sorted-lists/"
            ),
            Question("Linked List Cycle", "https://leetcode.com/problems/linked-list-cycle/"),
            Question(
                "Remove Nth Node From End of List",
                "https://leetcode.com/problems/remove-nth-node-from-end-of-list/"
            ),
            Question("Add Two Numbers", "https://leetcode.com/problems/add-two-numbers/"),
            Question(
                "Intersection of Two Linked Lists",
                "https://leetcode.com/problems/intersection-of-two-linked-lists/"
            ),
            Question(
                "Palindrome Linked List",
                "https://leetcode.com/problems/palindrome-linked-list/"
            ),
            Question(
                "Remove Linked List Elements",
                "https://leetcode.com/problems/remove-linked-list-elements/"
            ),
            Question("Odd Even Linked List", "https://leetcode.com/problems/odd-even-linked-list/"),
            Question("Swap Nodes in Pairs", "https://leetcode.com/problems/swap-nodes-in-pairs/"),
            Question("Sort List", "https://leetcode.com/problems/sort-list/"),
            Question("Reorder List", "https://leetcode.com/problems/reorder-list/"),
            Question("Rotate List", "https://leetcode.com/problems/rotate-list/"),
            Question(
                "Copy List with Random Pointer",
                "https://leetcode.com/problems/copy-list-with-random-pointer/"
            ),
            Question("LRU Cache", "https://leetcode.com/problems/lru-cache/")
        )
    ),
    Topic(
        topicName = "Recursion",
        topicDetails = "Problems solved using recursive algorithms",
        questions = listOf(
            Question("Fibonacci Number", "https://leetcode.com/problems/fibonacci-number/"),
            Question("Pow(x, n)", "https://leetcode.com/problems/powx-n/"),
            Question("Reverse String", "https://leetcode.com/problems/reverse-string/"),
            Question("Subsets", "https://leetcode.com/problems/subsets/"),
            Question("Permutations", "https://leetcode.com/problems/permutations/"),
            Question("Generate Parentheses", "https://leetcode.com/problems/generate-parentheses/"),
            Question(
                "Merge Two Sorted Lists",
                "https://leetcode.com/problems/merge-two-sorted-lists/"
            ),
            Question(
                "K-th Symbol in Grammar",
                "https://leetcode.com/problems/k-th-symbol-in-grammar/"
            ),
            Question("Climbing Stairs", "https://leetcode.com/problems/climbing-stairs/"),
            Question("N-Queens", "https://leetcode.com/problems/n-queens/"),
            Question("Sudoku Solver", "https://leetcode.com/problems/sudoku-solver/"),
            Question("Combination Sum", "https://leetcode.com/problems/combination-sum/"),
            Question("Word Search", "https://leetcode.com/problems/word-search/"),
            Question(
                "Letter Combinations of a Phone Number",
                "https://leetcode.com/problems/letter-combinations-of-a-phone-number/"
            ),
            Question(
                "Regular Expression Matching",
                "https://leetcode.com/problems/regular-expression-matching/"
            )
        )
    ),
    Topic(
        topicName = "Bit Manipulation",
        topicDetails = "Problems involving bitwise operations",
        questions = listOf(
            Question("Single Number", "https://leetcode.com/problems/single-number/"),
            Question("Number of 1 Bits", "https://leetcode.com/problems/number-of-1-bits/"),
            Question("Counting Bits", "https://leetcode.com/problems/counting-bits/"),
            Question("Reverse Bits", "https://leetcode.com/problems/reverse-bits/"),
            Question("Power of Two", "https://leetcode.com/problems/power-of-two/"),
            Question("Hamming Distance", "https://leetcode.com/problems/hamming-distance/"),
            Question("Sum of Two Integers", "https://leetcode.com/problems/sum-of-two-integers/"),
            Question("Missing Number", "https://leetcode.com/problems/missing-number/"),
            Question(
                "Bitwise AND of Numbers Range",
                "https://leetcode.com/problems/bitwise-and-of-numbers-range/"
            ),
            Question("Single Number II", "https://leetcode.com/problems/single-number-ii/"),
            Question("Single Number III", "https://leetcode.com/problems/single-number-iii/"),
            Question("UTF-8 Validation", "https://leetcode.com/problems/utf-8-validation/"),
            Question("Divide Two Integers", "https://leetcode.com/problems/divide-two-integers/"),
            Question("Subsets", "https://leetcode.com/problems/subsets/"),
            Question("Majority Element", "https://leetcode.com/problems/majority-element/")
        )
    ),
    Topic(
        topicName = "Stack and Queues",
        topicDetails = "Problems involving stack and queue data structures",
        questions = listOf(
            Question("Valid Parentheses", "https://leetcode.com/problems/valid-parentheses/"),
            Question("Min Stack", "https://leetcode.com/problems/min-stack/"),
            Question(
                "Implement Queue using Stacks",
                "https://leetcode.com/problems/implement-queue-using-stacks/"
            ),
            Question(
                "Implement Stack using Queues",
                "https://leetcode.com/problems/implement-stack-using-queues/"
            ),
            Question(
                "Evaluate Reverse Polish Notation",
                "https://leetcode.com/problems/evaluate-reverse-polish-notation/"
            ),
            Question("Daily Temperatures", "https://leetcode.com/problems/daily-temperatures/"),
            Question(
                "Next Greater Element I",
                "https://leetcode.com/problems/next-greater-element-i/"
            ),
            Question(
                "Backspace String Compare",
                "https://leetcode.com/problems/backspace-string-compare/"
            ),
            Question(
                "Remove All Adjacent Duplicates In String",
                "https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/"
            ),
            Question("Asteroid Collision", "https://leetcode.com/problems/asteroid-collision/"),
            Question("Basic Calculator", "https://leetcode.com/problems/basic-calculator/"),
            Question("Trapping Rain Water", "https://leetcode.com/problems/trapping-rain-water/"),
            Question(
                "Largest Rectangle in Histogram",
                "https://leetcode.com/problems/largest-rectangle-in-histogram/"
            ),
            Question(
                "Sliding Window Maximum",
                "https://leetcode.com/problems/sliding-window-maximum/"
            ),
            Question(
                "Design Circular Queue",
                "https://leetcode.com/problems/design-circular-queue/"
            )
        )
    ),
    Topic(
        topicName = "Sliding Window and Two Pointers",
        topicDetails = "Problems solved using sliding window or two-pointer techniques",
        questions = listOf(
            Question(
                "Longest Substring Without Repeating Characters",
                "https://leetcode.com/problems/longest-substring-without-repeating-characters/"
            ),
            Question(
                "Minimum Size Subarray Sum",
                "https://leetcode.com/problems/minimum-size-subarray-sum/"
            ),
            Question(
                "Longest Repeating Character Replacement",
                "https://leetcode.com/problems/longest-repeating-character-replacement/"
            ),
            Question(
                "Permutation in String",
                "https://leetcode.com/problems/permutation-in-string/"
            ),
            Question(
                "Find All Anagrams in a String",
                "https://leetcode.com/problems/find-all-anagrams-in-a-string/"
            ),
            Question(
                "Sliding Window Maximum",
                "https://leetcode.com/problems/sliding-window-maximum/"
            ),
            Question(
                "Container With Most Water",
                "https://leetcode.com/problems/container-with-most-water/"
            ),
            Question("3Sum", "https://leetcode.com/problems/3sum/"),
            Question(
                "Remove Duplicates from Sorted Array",
                "https://leetcode.com/problems/remove-duplicates-from-sorted-array/"
            ),
            Question("Trapping Rain Water", "https://leetcode.com/problems/trapping-rain-water/"),
            Question("Sort Colors", "https://leetcode.com/problems/sort-colors/"),
            Question(
                "Longest Mountain in Array",
                "https://leetcode.com/problems/longest-mountain-in-array/"
            ),
            Question(
                "Minimum Window Substring",
                "https://leetcode.com/problems/minimum-window-substring/"
            ),
            Question(
                "Subarrays with K Different Integers",
                "https://leetcode.com/problems/subarrays-with-k-different-integers/"
            ),
            Question(
                "Max Consecutive Ones III",
                "https://leetcode.com/problems/max-consecutive-ones-iii/"
            )
        )
    ),
    Topic(
        topicName = "Heaps",
        topicDetails = "Problems involving heap data structure",
        questions = listOf(
            Question(
                "Kth Largest Element in an Array",
                "https://leetcode.com/problems/kth-largest-element-in-an-array/"
            ),
            Question(
                "Top K Frequent Elements",
                "https://leetcode.com/problems/top-k-frequent-elements/"
            ),
            Question(
                "Find Median from Data Stream",
                "https://leetcode.com/problems/find-median-from-data-stream/"
            ),
            Question("Merge K Sorted Lists", "https://leetcode.com/problems/merge-k-sorted-lists/"),
            Question(
                "Sliding Window Median",
                "https://leetcode.com/problems/sliding-window-median/"
            ),
            Question(
                "K Closest Points to Origin",
                "https://leetcode.com/problems/k-closest-points-to-origin/"
            ),
            Question("Ugly Number II", "https://leetcode.com/problems/ugly-number-ii/"),
            Question(
                "Furthest Building You Can Reach",
                "https://leetcode.com/problems/furthest-building-you-can-reach/"
            ),
            Question(
                "Kth Smallest Element in a Sorted Matrix",
                "https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/"
            ),
            Question("Reorganize String", "https://leetcode.com/problems/reorganize-string/"),
            Question(
                "Smallest Range Covering Elements from K Lists",
                "https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/"
            ),
            Question("IPO", "https://leetcode.com/problems/ipo/"),
            Question(
                "Find K Pairs with Smallest Sums",
                "https://leetcode.com/problems/find-k-pairs-with-smallest-sums/"
            ),
            Question(
                "Trapping Rain Water II",
                "https://leetcode.com/problems/trapping-rain-water-ii/"
            ),
            Question(
                "Minimum Cost to Hire K Workers",
                "https://leetcode.com/problems/minimum-cost-to-hire-k-workers/"
            )
        )
    ),
    Topic(
        topicName = "Greedy Algorithms",
        topicDetails = "Problems solved using greedy approach",
        questions = listOf(
            Question("Jump Game", "https://leetcode.com/problems/jump-game/"),
            Question(
                "Best Time to Buy and Sell Stock II",
                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/"
            ),
            Question("Gas Station", "https://leetcode.com/problems/gas-station/"),
            Question("Assign Cookies", "https://leetcode.com/problems/assign-cookies/"),
            Question("Lemonade Change", "https://leetcode.com/problems/lemonade-change/"),
            Question("Maximum Subarray", "https://leetcode.com/problems/maximum-subarray/"),
            Question("Partition Labels", "https://leetcode.com/problems/partition-labels/"),
            Question(
                "Queue Reconstruction by Height",
                "https://leetcode.com/problems/queue-reconstruction-by-height/"
            ),
            Question(
                "Minimum Number of Arrows to Burst Balloons",
                "https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/"
            ),
            Question(
                "Non-overlapping Intervals",
                "https://leetcode.com/problems/non-overlapping-intervals/"
            ),
            Question("Task Scheduler", "https://leetcode.com/problems/task-scheduler/"),
            Question("Candy", "https://leetcode.com/problems/candy/"),
            Question("Remove K Digits", "https://leetcode.com/problems/remove-k-digits/"),
            Question(
                "Create Maximum Number",
                "https://leetcode.com/problems/create-maximum-number/"
            ),
            Question(
                "Minimum Deletions to Make Character Frequencies Unique",
                "https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/"
            )
        )
    ),
    Topic(
        topicName = "Binary Trees",
        topicDetails = "Problems involving binary tree data structure",
        questions = listOf(
            Question(
                "Maximum Depth of Binary Tree",
                "https://leetcode.com/problems/maximum-depth-of-binary-tree/"
            ),
            Question("Invert Binary Tree", "https://leetcode.com/problems/invert-binary-tree/"),
            Question("Symmetric Tree", "https://leetcode.com/problems/symmetric-tree/"),
            Question(
                "Binary Tree Level Order Traversal",
                "https://leetcode.com/problems/binary-tree-level-order-traversal/"
            ),
            Question(
                "Construct Binary Tree from Preorder and Inorder Traversal",
                "https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/"
            ),
            Question("Path Sum", "https://leetcode.com/problems/path-sum/"),
            Question(
                "Binary Tree Maximum Path Sum",
                "https://leetcode.com/problems/binary-tree-maximum-path-sum/"
            ),
            Question(
                "Lowest Common Ancestor of a Binary Tree",
                "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/"
            ),
            Question(
                "Binary Tree Right Side View",
                "https://leetcode.com/problems/binary-tree-right-side-view/"
            ),
            Question(
                "Serialize and Deserialize Binary Tree",
                "https://leetcode.com/problems/serialize-and-deserialize-binary-tree/"
            ),
            Question(
                "Binary Tree Zigzag Level Order Traversal",
                "https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/"
            ),
            Question(
                "Flatten Binary Tree to Linked List",
                "https://leetcode.com/problems/flatten-binary-tree-to-linked-list/"
            ),
            Question(
                "Populating Next Right Pointers in Each Node",
                "https://leetcode.com/problems/populating-next-right-pointers-in-each-node/"
            ),
            Question(
                "Validate Binary Tree Nodes",
                "https://leetcode.com/problems/validate-binary-tree-nodes/"
            ),
            Question(
                "Recover Binary Search Tree",
                "https://leetcode.com/problems/recover-binary-search-tree/"
            )
        )
    ),
    Topic(
        topicName = "Binary Search Trees",
        topicDetails = "Problems specific to binary search tree data structure",
        questions = listOf(
            Question(
                "Validate Binary Search Tree",
                "https://leetcode.com/problems/validate-binary-search-tree/"
            ),
            Question(
                "Lowest Common Ancestor of a Binary Search Tree",
                "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/"
            ),
            Question(
                "Kth Smallest Element in a BST",
                "https://leetcode.com/problems/kth-smallest-element-in-a-bst/"
            ),
            Question(
                "Convert Sorted Array to Binary Search Tree",
                "https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/"
            ),
            Question("Delete Node in a BST", "https://leetcode.com/problems/delete-node-in-a-bst/"),
            Question(
                "Binary Search Tree Iterator",
                "https://leetcode.com/problems/binary-search-tree-iterator/"
            ),
            Question(
                "Unique Binary Search Trees",
                "https://leetcode.com/problems/unique-binary-search-trees/"
            ),
            Question(
                "Recover Binary Search Tree",
                "https://leetcode.com/problems/recover-binary-search-tree/"
            ),
            Question("Balanced Binary Tree", "https://leetcode.com/problems/balanced-binary-tree/"),
            Question(
                "Convert BST to Greater Tree",
                "https://leetcode.com/problems/convert-bst-to-greater-tree/"
            ),
            Question(
                "Trim a Binary Search Tree",
                "https://leetcode.com/problems/trim-a-binary-search-tree/"
            ),
            Question("Range Sum of BST", "https://leetcode.com/problems/range-sum-of-bst/"),
            Question(
                "Construct Binary Search Tree from Preorder Traversal",
                "https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/"
            ),
            Question(
                "Serialize and Deserialize BST",
                "https://leetcode.com/problems/serialize-and-deserialize-bst/"
            ),
            Question(
                "Closest Binary Search Tree Value",
                "https://leetcode.com/problems/closest-binary-search-tree-value/"
            )
        )
    ),
    Topic(
        topicName = "Graphs",
        topicDetails = "Problems involving graph data structures and algorithms",
        questions = listOf(
            Question("Number of Islands", "https://leetcode.com/problems/number-of-islands/"),
            Question("Clone Graph", "https://leetcode.com/problems/clone-graph/"),
            Question("Course Schedule", "https://leetcode.com/problems/course-schedule/"),
            Question(
                "Pacific Atlantic Water Flow",
                "https://leetcode.com/problems/pacific-atlantic-water-flow/"
            ),
            Question("Graph Valid Tree", "https://leetcode.com/problems/graph-valid-tree/"),
            Question("Word Ladder", "https://leetcode.com/problems/word-ladder/"),
            Question("Alien Dictionary", "https://leetcode.com/problems/alien-dictionary/"),
            Question("Network Delay Time", "https://leetcode.com/problems/network-delay-time/"),
            Question("Evaluate Division", "https://leetcode.com/problems/evaluate-division/"),
            Question("Redundant Connection", "https://leetcode.com/problems/redundant-connection/"),
            Question("Is Graph Bipartite?", "https://leetcode.com/problems/is-graph-bipartite/"),
            Question(
                "Cheapest Flights Within K Stops",
                "https://leetcode.com/problems/cheapest-flights-within-k-stops/"
            ),
            Question(
                "Reconstruct Itinerary",
                "https://leetcode.com/problems/reconstruct-itinerary/"
            ),
            Question("Accounts Merge", "https://leetcode.com/problems/accounts-merge/"),
            Question(
                "Critical Connections in a Network",
                "https://leetcode.com/problems/critical-connections-in-a-network/"
            )
        )
    ),
    Topic(
        topicName = "Dynamic Programming",
        topicDetails = "Problems solved using dynamic programming techniques",
        questions = listOf(
            Question("Climbing Stairs", "https://leetcode.com/problems/climbing-stairs/"),
            Question("Coin Change", "https://leetcode.com/problems/coin-change/"),
            Question(
                "Longest Increasing Subsequence",
                "https://leetcode.com/problems/longest-increasing-subsequence/"
            ),
            Question("Word Break", "https://leetcode.com/problems/word-break/"),
            Question("Combination Sum IV", "https://leetcode.com/problems/combination-sum-iv/"),
            Question("House Robber", "https://leetcode.com/problems/house-robber/"),
            Question("Decode Ways", "https://leetcode.com/problems/decode-ways/"),
            Question("Unique Paths", "https://leetcode.com/problems/unique-paths/"),
            Question("Jump Game", "https://leetcode.com/problems/jump-game/"),
            Question(
                "Palindromic Substrings",
                "https://leetcode.com/problems/palindromic-substrings/"
            ),
            Question(
                "Longest Palindromic Subsequence",
                "https://leetcode.com/problems/longest-palindromic-subsequence/"
            ),
            Question("Edit Distance", "https://leetcode.com/problems/edit-distance/"),
            Question("Burst Balloons", "https://leetcode.com/problems/burst-balloons/"),
            Question(
                "Regular Expression Matching",
                "https://leetcode.com/problems/regular-expression-matching/"
            ),
            Question("Maximal Rectangle", "https://leetcode.com/problems/maximal-rectangle/")
        )
    ),
    Topic(
        topicName = "Tries",
        topicDetails = "Advanced tree-like data structure for efficient string operations",
        questions = listOf(
            Question(
                "Implement Trie (Prefix Tree)",
                "https://leetcode.com/problems/implement-trie-prefix-tree/"
            ),
            Question(
                "Design Add and Search Words Data Structure",
                "https://leetcode.com/problems/design-add-and-search-words-data-structure/"
            ),
            Question("Word Search II", "https://leetcode.com/problems/word-search-ii/"),
            Question("Replace Words", "https://leetcode.com/problems/replace-words/"),
            Question(
                "Maximum XOR of Two Numbers in an Array",
                "https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/"
            ),
            Question("Map Sum Pairs", "https://leetcode.com/problems/map-sum-pairs/"),
            Question(
                "Implement Magic Dictionary",
                "https://leetcode.com/problems/implement-magic-dictionary/"
            ),
            Question("Palindrome Pairs", "https://leetcode.com/problems/palindrome-pairs/"),
            Question(
                "Design Search Autocomplete System",
                "https://leetcode.com/problems/design-search-autocomplete-system/"
            ),
            Question("Stream of Characters", "https://leetcode.com/problems/stream-of-characters/"),
            Question("Word Squares", "https://leetcode.com/problems/word-squares/"),
            Question("Concatenated Words", "https://leetcode.com/problems/concatenated-words/"),
            Question(
                "Prefix and Suffix Search",
                "https://leetcode.com/problems/prefix-and-suffix-search/"
            ),
            Question(
                "Short Encoding of Words",
                "https://leetcode.com/problems/short-encoding-of-words/"
            ),
            Question("Camelcase Matching", "https://leetcode.com/problems/camelcase-matching/")
        )
    )
)