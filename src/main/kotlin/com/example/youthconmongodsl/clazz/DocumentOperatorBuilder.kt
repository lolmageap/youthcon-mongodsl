package com.example.youthconmongodsl.clazz

import com.example.youthconmongodsl.clazz.DocumentOperator.AND
import com.example.youthconmongodsl.clazz.DocumentOperator.ELEM_MATCH
import com.example.youthconmongodsl.clazz.DocumentOperator.NOR
import com.example.youthconmongodsl.clazz.DocumentOperator.NOT
import com.example.youthconmongodsl.clazz.DocumentOperator.OR
import org.bson.Document

class DocumentOperatorBuilder(
    val document: Document,
) {
    fun and(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) document.append(AND, notEmptyBlocks)
        else document
    }

    fun or(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) document.append(OR, notEmptyBlocks)
        else document
    }

    fun nor(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) document.append(NOR, notEmptyBlocks)
        else document
    }

    fun not(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) document.append(NOT, notEmptyBlocks)
        else document
    }

    fun Document.and(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(AND, notEmptyBlocks)
        else this
    }

    fun Document.or(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(OR, notEmptyBlocks)
        else this
    }

    fun Document.nor(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(NOR, notEmptyBlocks)
        else this
    }

    fun Document.not(
        vararg block: Document.() -> Document,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(NOT, notEmptyBlocks)
        else this
    }

    // elemMatch 는 아직 테스트를 해보지 않았습니다.
    fun Document.elemMatch(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(ELEM_MATCH, notEmptyBlocks)
        else this
    }

    fun Document.and(
        block: List<Document.() -> Document?>,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(AND, notEmptyBlocks)
        else this
    }

    fun Document.or(
        block: List<Document.() -> Document?>,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(OR, notEmptyBlocks)
        else this
    }

    fun Document.nor(
        block: List<Document.() -> Document?>,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(NOR, notEmptyBlocks)
        else this
    }

    fun Document.not(
        block: List<Document.() -> Document?>,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(NOT, notEmptyBlocks)
        else this
    }

    // elemMatch 는 아직 테스트를 해보지 않았습니다.
    fun Document.elemMatch(
        block: List<Document.() -> Document?>,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(ELEM_MATCH, notEmptyBlocks)
        else this
    }

    private fun Array<out Document.() -> Document?>.invokeIfNotEmpty() =
        this.mapNotNull {
            val doc = Document().it()
            doc?.ifEmpty { null }
        }

    private fun List<Document.() -> Document?>.invokeIfNotEmpty() =
        this.mapNotNull {
            val doc = Document().it()
            doc?.ifEmpty { null }
        }

    private fun run(
        block: DocumentOperatorBuilder.() -> Unit,
    ): Document {
        block.invoke(this)
        return document
    }

    companion object {
        operator fun invoke(
            document: Document,
            block: DocumentOperatorBuilder.() -> Unit,
        ): Document {
            return DocumentOperatorBuilder(document)
                .run(block)
        }
    }
}