/*
 * Copyright (C) 2016 AdvancingPainters (https://github.com/AdvancingPainters).
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

package com.ease.data;


import com.ease.model.BaseModel;

import java.util.List;

import rx.Observable;

/**
 * data action interface
 * Created by Spencer on 15/10/28.
 */
public interface DataActionInterface<M extends BaseModel> {

    Observable<List<M>> doInitialize();

    Observable<List<M>> doRefresh();

    Observable<List<M>> doLoadMore();
}
