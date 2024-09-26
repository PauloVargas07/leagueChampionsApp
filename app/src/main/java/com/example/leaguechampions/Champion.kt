package com.example.leaguechampions

data class Champion(
    val id : String,
    val key: String,
    val name: String,
    val title: String,
    val tags: Array<String>,
    val stats : ChampionStats,
    val icon: String,
    val sprite : ChampionSprite,
    val description: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Champion

        if (id != other.id) return false
        if (key != other.key) return false
        if (name != other.name) return false
        if (title != other.title) return false
        if (!tags.contentEquals(other.tags)) return false
        if (stats != other.stats) return false
        if (icon != other.icon) return false
        if (sprite != other.sprite) return false
        if (description != other.description) return false

        return true
    }

    override fun toString(): String {
        return "Champion(id='$id', key='$key', name='$name', title='$title', tags=${tags.contentToString()}, stats=$stats, icon='$icon', sprite=$sprite, description='$description')"
    }


}