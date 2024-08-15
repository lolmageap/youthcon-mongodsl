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
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) document.append(AND, notEmptyBlock)
        else document
    }

    fun and(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) document.append(AND, notEmptyBlocks)
        else document
    }

    fun or(
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) document.append(OR, notEmptyBlock)
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
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) document.append(NOR, notEmptyBlock)
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
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) document.append(NOT, notEmptyBlock)
        else document
    }

    fun not(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) document.append(NOT, notEmptyBlocks)
        else document
    }

    fun EmbeddedDocument.elemMatch(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()

        return if (notEmptyBlocks.isNotEmpty()) {
            val mergedDocument = Document()
            notEmptyBlocks.forEach { mergedDocument.putAll(it) }

            document.append(this.name, Document().append(ELEM_MATCH, mergedDocument))
        } else document
    }

    fun EmbeddedDocument.elemMatch(
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) document.append(this.name, Document().append(ELEM_MATCH, notEmptyBlock))
        else document
    }

    fun Document.and(
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = invokeIfNotEmpty(block)
        return if (notEmptyBlocks != null) this.append(AND, notEmptyBlocks)
        else this
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

    fun Document.or(
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) this.append(OR, notEmptyBlock)
        else this
    }

    fun Document.nor(
        vararg block: Document.() -> Document?,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(NOR, notEmptyBlocks)
        else this
    }

    fun Document.nor(
        block: Document.() -> Document?,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) this.append(NOR, notEmptyBlock)
        else this
    }

    fun Document.not(
        vararg block: Document.() -> Document,
    ): Document {
        val notEmptyBlocks = block.invokeIfNotEmpty()
        return if (notEmptyBlocks.isNotEmpty()) this.append(NOT, notEmptyBlocks)
        else this
    }

    fun Document.not(
        block: Document.() -> Document,
    ): Document {
        val notEmptyBlock = invokeIfNotEmpty(block)
        return if (notEmptyBlock != null) this.append(NOT, notEmptyBlock)
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

    private fun invokeIfNotEmpty(
        block: Document.() -> Document?,
    ): Document? {
        val doc = Document().block()
        return doc?.ifEmpty { null }
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