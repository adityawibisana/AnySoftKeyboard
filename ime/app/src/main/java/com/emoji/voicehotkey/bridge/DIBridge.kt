package com.emoji.voicehotkey.bridge

object DIBridge {
    lateinit var configurationOptionViewProvider: ConfigurationOptionViewProvider
        private set

    fun register(provider: ConfigurationOptionViewProvider) {
        configurationOptionViewProvider = provider
    }
}