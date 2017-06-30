/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.reactivex.common.exceptions;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.reactivex.common.TestCommonHelper;
import io.reactivex.common.internal.utils.ExceptionHelper;

public class ExceptionsTest {

    @Ignore("Exceptions is not an enum")
    @Test
    public void constructorShouldBePrivate() {
        TestCommonHelper.checkUtilityClass(ExceptionHelper.class);
    }

    @Test
    public void utilityClass() {
        TestCommonHelper.checkUtilityClass(Exceptions.class);
    }

    @Test
    public void manualThrowIfFatal() {

        try {
            Exceptions.throwIfFatal(new ThreadDeath());
            fail("Didn't throw fatal exception");
        } catch (ThreadDeath ex) {
            // expected
        }

        try {
            Exceptions.throwIfFatal(new LinkageError());
            fail("Didn't throw fatal error");
        } catch (LinkageError ex) {
            // expected
        }

        try {
            ExceptionHelper.wrapOrThrow(new LinkageError());
            fail("Didn't propagate Error");
        } catch (LinkageError ex) {
            // expected
        }
    }

    @Test
    public void manualPropagate() {

        try {
            Exceptions.propagate(new InternalError());
            fail("Didn't throw exception");
        } catch (InternalError ex) {
            // expected
        }

        try {
            throw Exceptions.propagate(new IllegalArgumentException());
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            throw ExceptionHelper.wrapOrThrow(new IOException());
        } catch (RuntimeException ex) {
            if (!(ex.getCause() instanceof IOException)) {
                fail(ex.toString() + ": should have thrown RuntimeException(IOException)");
            }
        }
    }

    @Test
    public void errorNotImplementedNull1() {
        OnErrorNotImplementedException ex = new OnErrorNotImplementedException(null);

        assertTrue("" + ex.getCause(), ex.getCause() instanceof NullPointerException);
    }

    @Test
    public void errorNotImplementedNull2() {
        OnErrorNotImplementedException ex = new OnErrorNotImplementedException("Message", null);

        assertTrue("" + ex.getCause(), ex.getCause() instanceof NullPointerException);
    }

    @Test
    public void errorNotImplementedWithCause() {
        OnErrorNotImplementedException ex = new OnErrorNotImplementedException("Message", new TestException("Forced failure"));

        assertTrue("" + ex.getCause(), ex.getCause() instanceof TestException);

        assertEquals("" + ex.getCause(), "Forced failure", ex.getCause().getMessage());
    }

}
