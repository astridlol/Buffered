package sh.astrid.buffered.data

data class Tag(
        val displayName: String,
        val tag: String,
        val earnDescription: String = "<t><bold>»</bold> /buy <bold>«</bold>"
)

data class Tags(
        val tags: Map<String, Tag>
)