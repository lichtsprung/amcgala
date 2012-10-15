/*
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.amcgala.framework.raytracer;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 10/15/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class HitResult {
    public static final HitResult NO_HIT = new HitResult();
    protected double t = Double.POSITIVE_INFINITY;
}
