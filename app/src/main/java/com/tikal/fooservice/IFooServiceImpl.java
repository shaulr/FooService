package com.tikal.fooservice;

import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IFooServiceImpl extends IFooService.Stub {
    private final static String TAG = "IFooServiceImpl";
    private Map<Integer, Bar> bars;
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    @Override
    public void save(Bar bar) throws RemoteException {
        Log.d(TAG, "save bar " + bar.getData());
        if(bars == null) {
            bars = Collections.synchronizedMap(new HashMap<Integer, Bar>());
        }
        bars.put(bar.getId(), bar);
    }

    @Override
    public Bar getById(int id) throws RemoteException {
        Log.d(TAG, "get by ID " + id);

        if(bars == null || !bars.containsKey(id)) {
            throw new RemoteException("no such bar");
        }

        return bars.get(id);
    }

    @Override
    public void delete(Bar bar) throws RemoteException {
        Log.d(TAG, "delete bar " + (bar == null ? "null" : bar.getId()));

        if(bars == null || bar == null || !bars.containsKey(bar.getId())) {
            throw new RemoteException("no such bar");
        }
        bars.remove(bar.getId());
    }

    @Override
    public List<Bar> getAll() throws RemoteException {
        return new ArrayList<>(bars.values());
    }

    @Override
    public void asyncGetList(final IFooResponseListner listener) throws RemoteException {
        Log.d(TAG, "asyncGetList call");

        worker.schedule(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "asyncGetList is being executed");

                StringBuilder sb = new StringBuilder();
                Iterator<Map.Entry<Integer, Bar>> iter = bars.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<Integer, Bar> entry = iter.next();
                    sb.append(entry.getKey());
                    sb.append('=').append('"');
                    sb.append(entry.getValue().getData());
                    sb.append('"');
                    if (iter.hasNext()) {
                        sb.append(',').append(' ');
                    }
                    try {
                        listener.onResponse(sb.toString());
                    } catch (RemoteException e) {
                        Log.e(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }, 5, TimeUnit.SECONDS);
    }
}
