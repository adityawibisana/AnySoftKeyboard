package com.emoji.voicehotkey.bridge

object DIBridge {
    lateinit var configurationOptionViewProvider: ConfigurationOptionViewProvider
        private set

    lateinit var listener: () -> Unit
        private set

    fun register(provider: ConfigurationOptionViewProvider) {
        configurationOptionViewProvider = provider
    }

    fun setDialogCloseAction(listener: () -> Unit) {
        this.listener = listener
    }
}