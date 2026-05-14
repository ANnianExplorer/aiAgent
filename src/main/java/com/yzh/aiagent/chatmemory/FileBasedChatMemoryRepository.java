package com.yzh.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件持久化的 ChatMemoryRepository
 *
 * @author yzh
 * @since 2026/5/14
 */
public class FileBasedChatMemoryRepository implements ChatMemoryRepository {

    /**
     * 文件存储目录
     */
    private final String BASE_DIR;

    /**
     * Kryo序列化
     */
    private static final Kryo kryo = new Kryo();

    static {

        // 不强制注册
        kryo.setRegistrationRequired(false);

        // 解决无参构造问题
        kryo.setInstantiatorStrategy(
                new StdInstantiatorStrategy()
        );
    }

    /**
     * 构造器
     */
    public FileBasedChatMemoryRepository(String dir) {

        this.BASE_DIR = dir;

        File baseDir = new File(dir);

        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /**
     * 查询所有会话ID
     * 新版 Spring AI 中，ChatMemoryRepository 不再只是“按会话读写消息”，
     * 还增加了“会话列表管理”能力，所以必须实现 findConversationIds()。
     */
    @Override
    public List<String> findConversationIds() {

        File baseDir = new File(BASE_DIR);
        File[] files = baseDir.listFiles();
        if (files == null || files.length == 0) {
            return List.of();
        }
        List<String> conversationIds = new ArrayList<>();

        for (File file : files) {

            String fileName = file.getName();

            if (fileName.endsWith(".kryo")) {

                conversationIds.add(
                        fileName.replace(".kryo", "")
                );
            }
        }

        return conversationIds;
    }

    /**
     * 查询会话消息
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        File file = getConversationFile(conversationId);
        // 文件不存在
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Input input = new Input(new FileInputStream(file))) {
            return kryo.readObject(input, ArrayList.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存会话消息
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除会话
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取会话文件
     */
    private File getConversationFile(String conversationId) {
        return new File(
                BASE_DIR,
                conversationId + ".kryo"
        );
    }
}