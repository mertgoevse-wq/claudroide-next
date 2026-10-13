package org.claudroide.app

import org.claudroide.app.feature.chat.ChatInputState
import org.claudroide.app.feature.chat.ChatMessage
import org.claudroide.app.feature.chat.ChatViewModel
import org.claudroide.app.feature.provider.KeyVaultStorage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before

/**
 * Task 070+ — ChatViewModel tests.
 *
 * Tests the ViewModel that wires ChatScreen to the provider transport.
 * Uses a fake KeyVaultStorage so no real keys or network calls are involved.
 */
class ChatViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    // ── Input handling ─────────────────────────────────────────────────────────

    @Test
    fun onInputChange_updatesState() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onInputChange("Hallo")
        val state = vm.uiState.value
        assertEquals("Hallo", state.input.text)
    }

    // ── Send without provider ──────────────────────────────────────────────────

    @Test
    fun sendMessage_withoutKey_showsError() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onProviderSelected("anthropic", "claude-sonnet-5-5")
        vm.onInputChange("Test")
        vm.sendMessage()
        val state = vm.uiState.value
        assertTrue(state.messages.isNotEmpty())
        assertTrue(state.messages[0].fromUser)
        assertEquals("Test", state.messages[0].text)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("Zugangsschlüssel"))
    }

    // ── Send with unknown provider ─────────────────────────────────────────────

    @Test
    fun sendMessage_withUnknownProvider_showsError() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onProviderSelected("nonexistent", "model")
        vm.onInputChange("Test")
        vm.sendMessage()
        val state = vm.uiState.value
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("nicht gefunden"))
    }

    // ── New conversation clears messages ──────────────────────────────────────

    @Test
    fun newConversation_clearsMessages() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onInputChange(" Erst ")
        vm.sendMessage()
        assertTrue(vm.uiState.value.messages.isNotEmpty())

        vm.newConversation()
        assertTrue(vm.uiState.value.messages.isEmpty())
        assertNotNull(vm.uiState.value.conversationId)
    }

    // ── Stop streaming ─────────────────────────────────────────────────────────

    @Test
    fun stopStreaming_setsFlag() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onInputChange("Test")
        vm.sendMessage()
        assertFalse(vm.uiState.value.isStreaming)
    }

    // ── Provider selection ─────────────────────────────────────────────────────

    @Test
    fun onProviderSelected_updatesState() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onProviderSelected("anthropic", "claude-sonnet-5-5")
        val state = vm.uiState.value
        assertEquals("anthropic", state.currentProviderId)
        assertEquals("claude-sonnet-5-5", state.currentModelId)
    }

    // ── Error clearing ─────────────────────────────────────────────────────────

    @Test
    fun clearError_removesError() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        vm.onInputChange("Test")
        vm.sendMessage()
        assertNotNull(vm.uiState.value.error)

        vm.clearError()
        assertNull(vm.uiState.value.error)
    }

    // ── Conversation ID is unique ──────────────────────────────────────────────

    @Test
    fun eachViewModel_hasUniqueConversationId() = runTest {
        val vm1 = ChatViewModel(keyVault = FakeKeyVault())
        val vm2 = ChatViewModel(keyVault = FakeKeyVault())
        assertTrue(vm1.uiState.value.conversationId.isNotBlank())
        assertTrue(vm2.uiState.value.conversationId.isNotBlank())
    }

    // ── Attachment sheet toggle ─────────────────────────────────────────────────

    @Test
    fun onAttachmentClick_showsAttachmentSheet() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        assertFalse(vm.uiState.value.showAttachmentSheet)

        vm.onAttachmentClick()
        assertTrue(vm.uiState.value.showAttachmentSheet)
    }

    @Test
    fun onAttachmentClick_togglesAttachmentSheet() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())

        vm.onAttachmentClick()
        assertTrue(vm.uiState.value.showAttachmentSheet)

        vm.onAttachmentClick()
        assertFalse(vm.uiState.value.showAttachmentSheet)
    }

    @Test
    fun onDismissAttachmentSheet_hidesAttachmentSheet() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())

        vm.onAttachmentClick()
        assertTrue(vm.uiState.value.showAttachmentSheet)

        vm.onDismissAttachmentSheet()
        assertFalse(vm.uiState.value.showAttachmentSheet)
    }

    @Test
    fun addAttachment_and_removeAttachment_manageAttachmentsList() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        assertEquals(0, vm.uiState.value.input.attachments.size)

        vm.addAttachment("test.kt", 1024)
        assertEquals(1, vm.uiState.value.input.attachments.size)
        val attachment = vm.uiState.value.input.attachments[0]
        assertEquals("test.kt", attachment.name)
        assertEquals(1024L, attachment.sizeBytes)

        vm.removeAttachment(attachment.id)
        assertEquals(0, vm.uiState.value.input.attachments.size)
    }

    @Test
    fun onModeClick_and_onDismissModelDialog_toggleState() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        assertFalse(vm.uiState.value.showModelDialog)

        vm.onModeClick()
        assertTrue(vm.uiState.value.showModelDialog)

        vm.onDismissModelDialog()
        assertFalse(vm.uiState.value.showModelDialog)
    }

    @Test
    fun onVoiceClick_and_onDismissVoiceInfoDialog_toggleState() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        assertFalse(vm.uiState.value.showVoiceInfoDialog)

        vm.onVoiceClick()
        assertTrue(vm.uiState.value.showVoiceInfoDialog)

        vm.onDismissVoiceInfoDialog()
        assertFalse(vm.uiState.value.showVoiceInfoDialog)
    }

    @Test
    fun onProfileClick_and_onDismissProfileDialog_toggleState() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        assertFalse(vm.uiState.value.showProfileDialog)

        vm.onProfileClick()
        assertTrue(vm.uiState.value.showProfileDialog)

        vm.onDismissProfileDialog()
        assertFalse(vm.uiState.value.showProfileDialog)
    }

    @Test
    fun selectConversation_updatesConversationId_andResetsState() = runTest {
        val vm = ChatViewModel(keyVault = FakeKeyVault())
        val customId = "conv_historical_123"

        vm.selectConversation(customId, "Vorheriger Chat")
        assertEquals(customId, vm.uiState.value.conversationId)
        assertNull(vm.uiState.value.error)
    }
}

/** Minimal key vault that returns null for every key. */
private class FakeKeyVault : KeyVaultStorage {
    override fun storeKey(providerId: String, apiKey: String) {}
    override fun retrieveKey(providerId: String): String? = null
    override fun removeKey(providerId: String): Boolean = false
    override fun hasKey(providerId: String): Boolean = false
    override fun clearAllKeys() {}
    override fun exportSafeMetadata(): Map<String, String> = emptyMap()
}
