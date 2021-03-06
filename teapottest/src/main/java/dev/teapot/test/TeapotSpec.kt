package dev.teapot.test

import dev.teapot.cmd.BatchCmd
import dev.teapot.cmd.Cmd
import dev.teapot.contract.State
import dev.teapot.contract.Upd
import dev.teapot.msg.Msg
import org.junit.Assert
import org.junit.Assert.assertEquals


class TeapotSpec<S : State> constructor(val feature: Upd<S>) {

    private lateinit var state: S
    private var prevState: S? = null
    private lateinit var cmd: Cmd

    fun withState(state: S): TeapotSpec<S> {
        this.state = state
        return this
    }

    fun withState(state: S, oldState: S): TeapotSpec<S> {
        this.state = state
        this.prevState = oldState
        return this
    }

    fun withCmd(c: Cmd): TeapotSpec<S> {
        this.cmd = c
        return this
    }

    fun state(): S {
        return state
    }

    fun copy(): TeapotSpec<S> {
        return TeapotSpec(feature).withCmd(this.cmd).withState(this.state)
    }

    fun whenMsg(msg: Msg): TeapotSpec<S> {
        val update = feature.update(msg, state)
        val newState = update.updatedState
        val cmd = update.cmds
        return this.withState(newState ?: state, state).withCmd(cmd)
    }

    fun thenCmd(assertionCmd: Cmd): TeapotSpec<S> {
        assertEquals(assertionCmd, cmd)
        return this
    }

    fun andCmd(assertionCmd: Cmd): TeapotSpec<S> {
        return thenCmd(assertionCmd)
    }

    fun thenCmdBatch(vararg cmds: Cmd): TeapotSpec<S> {
        Assert.assertEquals(cmds.size, (this.cmd as BatchCmd).cmds.size)
        Assert.assertEquals(BatchCmd(cmds = cmds.toMutableSet()), this.cmd)
        return this
    }

    fun thenCmdBatchContains(vararg cmds: Cmd): TeapotSpec<S> {
        cmds.forEach {
            Assert.assertTrue((this.cmd as BatchCmd).cmds.contains(it))
        }
        return this
    }

    fun assertCmds(assert: (cmds: BatchCmd) -> Unit): TeapotSpec<S> {
        assert.invoke(this.cmd as BatchCmd)
        return this
    }

    fun assertCmd(assert: (cmds: Cmd) -> Unit): TeapotSpec<S> {
        assert.invoke(this.cmd)
        return this
    }

    fun andCmdBatch(vararg cmds: Cmd): TeapotSpec<S> {
        return thenCmdBatch(*cmds)
    }

    /** Asserts that state is exactly like [state] */
    fun andHasExactState(state: S): TeapotSpec<S> {
        Assert.assertEquals(state, this.state)
        return this
    }

    fun assertState(transform: (s: S) -> S): TeapotSpec<S> {
        Assert.assertEquals(transform(state), state)
        return this.withState(state)
    }

    fun diffState(transform: (prevState: S) -> S): TeapotSpec<S> {
        Assert.assertEquals(transform(prevState!!), state)
        return this.withState(state)
    }

    /**
     * alias to assertState
     */
    fun andState(transform: (s: S) -> S): TeapotSpec<S> {
        return assertState(transform)
    }

    /**
     * alias to assertState
     */
    fun thenState(transform: (s: S) -> S): TeapotSpec<S> {
        return assertState(transform)
    }

    fun checkState(assertion: (s: S) -> Unit): TeapotSpec<S> {
        assertion(this.state)
        return this
    }
}
